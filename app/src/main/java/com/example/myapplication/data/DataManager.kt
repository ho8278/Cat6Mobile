package com.example.myapplication.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.myapplication.data.local.db.DbHelper
import com.example.myapplication.data.local.db.DbHelperImpl
import com.example.myapplication.data.local.pref.PreferenceHelper
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.*
import com.example.myapplication.data.remote.api.ApiHelperImpl
import com.example.myapplication.data.remote.fcm.FCMHelperImpl
import com.example.myapplication.util.ChatSocketService
import com.example.myapplication.view.main.ErrorCode
import com.example.myapplication.view.references.Reference
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Okio
import java.io.File
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*


class DataManager : DataSource {

    private val apiHelper = ApiHelperImpl.api
    private val fcmApiHelper = FCMHelperImpl.api
    private val receiveSubject: Subject<ChatInfo>
    val TAG = DataManager::class.java.simpleName

    init {
        receiveSubject = PublishSubject.create()
        Log.e("DataManager", "Init")
    }

    companion object {
        private lateinit var dbHelper: DbHelper
        private lateinit var prefHelper: PreferenceHelper
        @Volatile
        private var INSTANCE: DataManager? = null

        fun getInstance(context: Context): DataManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataManager().apply {
                    dbHelper = DbHelperImpl.getInstance(context)
                    prefHelper = PreferenceHelperImpl.getInstance(context)
                    INSTANCE = this
                }
            }

    }

    override fun loadChatClient(): Single<List<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendMessage(chatInfo: ChatInfo):Single<ResponseBody> {
        val chatInfoJson = JsonObject().apply {
            addProperty("chatinfo_id", chatInfo.chatinfo_id)
            addProperty("message", chatInfo.message)
            addProperty("chatroom_id", chatInfo.chatroom_id)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            addProperty("send_date", dateFormat.format(chatInfo.send_date))
            addProperty("send_user_id", chatInfo.send_user_id)
        }

        prefHelper.saveItem(PreferenceHelperImpl.RECENT_CHATINFO_ID, chatInfo.chatinfo_id)
        ChatSocketService.socket?.emit("send", chatInfoJson)
        return Single.create {  }
    }

    override fun sendBroadCastMessage(chatRoomID: String): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("chatinfo_id", chatRoomID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun receiveMessage(chatInfo: ChatInfo) {
        dbHelper.insertChatInfo(chatInfo)
        receiveSubject.onNext(chatInfo)
    }

    override fun receiveMessage(): Observable<ChatInfo> {
        return receiveSubject.observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertChatInfo(chatInfo: ChatInfo) {
        dbHelper.insertChatInfo(chatInfo)
    }

    override fun loadChatInfoList(roomId: String): Single<List<ChatInfo>> {
        ChatSocketService.socket?.emit("channelJoin", roomId)
        return dbHelper.loadChatInfoList(roomId)
    }

    override fun <T : Any> saveItem(key: String, text: T) {
        prefHelper.saveItem(key, text)
    }

    override fun <T : Any> getItem(key: String): T {
        return prefHelper.getItem(key)
    }


    override fun loadUser(userID: String): Single<UserServerResponse> {
        return apiHelper.getUser(userID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun getCurrentUser(): Single<User> {
        return dbHelper.getUser(prefHelper.getItem(PreferenceHelperImpl.CURRENT_USER_ID))
    }

    override fun getUser(userID: String): Single<User> {
        return dbHelper.getUser(userID)
    }

    override fun loadSchedule(groupId: String): Single<List<Schedule>> {
        /*
        return apiHelper.loadSchedules(groupId)
            .doOnSuccess {
                Log.e(TAG, it.data.toString())
                dbHelper.updateSchedule(it.data)
            }
            .doOnError { Log.e(TAG, it.message) }
            .flatMap { it ->
                val code = it.responseCode.toInt()
                val list = it.data
                Single.create<ErrorCode> {
                    if(list.size==0)
                        it.onSuccess(ErrorCode.SUCCESS)
                    else if (code == ErrorCode.WRONG_PARAMETER.code)
                        it.onError(Throwable(ErrorCode.WRONG_PARAMETER.description))
                    else
                        it.onSuccess(ErrorCode.SUCCESS)
                }
            }
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            */
        return apiHelper.loadSchedules(groupId)
            .map { it.data }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getSchedules(year: Int, month: Int, day: Int): Single<List<Schedule>> {
        return dbHelper.getSchedules(year, month, day)
    }

    override fun saveSchedule(schedule: Schedule): Single<String> {
        val startDate = schedule.startDate
        val endDate = schedule.endDate

        if (startDate > endDate) {
            return Single.create<String> {
                it.onError(Throwable(ErrorCode.LATE_START_DATE.code.toString()))
            }
        }

        if (schedule.name.isEmpty()) {
            return Single.create<String> {
                it.onError(Throwable(ErrorCode.EMPTY_TEXT.code.toString()))
            }
        }

        return apiHelper.insertSchedule(startDate, endDate, schedule.name, schedule.teamID)
            .doOnSuccess { it ->
                dbHelper.insertSchedule(
                    Schedule(
                        it,
                        schedule.startDate,
                        schedule.endDate,
                        schedule.name,
                        schedule.teamID
                    )
                )
            }
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteSchedule(id: String): Single<Int> {
        return apiHelper.deleteSchedule(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateSchedulers(schedule: Schedule) {
        dbHelper.updateSchedule(schedule)
    }

    override fun insertTeam(team: Team) {
        dbHelper.insertTeam(team)
    }

    override fun getTeam(teamID: String): Single<Team> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadTeam(userID: String): Single<List<Team>> {
        return apiHelper.loadTeams(userID)
            .map { response -> response.data }
            .doOnError { Log.e(TAG, it.message) }
            .doOnSuccess { dbHelper.updateTeam(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addUserToTeam(userID: String): Single<ErrorCode> {
        return apiHelper.inviteTeam(userID,getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> ErrorCode.fromCode(response) }
    }

    override fun loadChatRoom(): Single<List<ChatRoom>> {
        Log.e(TAG,getItem(PreferenceHelperImpl.CURRENT_GROUP_ID) )
        return apiHelper.loadChatRooms(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .map { response -> response.data }
            .doOnSuccess {
                if(it.size !=0){
                    saveItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID, it[0].id)
                    dbHelper.updateChatRoom(it)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createChatRoom(clientID: String, chatRoomName: String): Single<ChatRoom> {
        var chatID = ""
        return apiHelper.createChatRoom(chatRoomName, prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .subscribeOn(Schedulers.io())
            .doOnError { Log.e(TAG, it.message) }
            .flatMap {
                chatID = it
                apiHelper.inviteChatRoom(clientID, chatID)
            }
            .flatMap { sendBroadCastMessage(chatID) }
            .flatMap { Single.create<ChatRoom> { it.onSuccess(ChatRoom(chatID, chatRoomName)) } }
            .observeOn(AndroidSchedulers.mainThread())

    }

    override fun inviteChatRoom(clientID: String): Single<Int> {
        return apiHelper.inviteChatRoom(clientID, prefHelper.getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID))
            .flatMap { sendBroadCastMessage(clientID, prefHelper.getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID)) }
            .map { response -> 1 }
            .subscribeOn(Schedulers.io())

    }

    private fun sendBroadCastMessage(clientID: String, chatRoomID: String): Single<ResponseBody> {
        val json = JsonObject().apply {
            addProperty(
                "to",
                "/topics/main"
            )
            addProperty("priority", "high")
            val element = JsonObject()
            element.addProperty("chatinfo_id", chatRoomID)
            element.addProperty("who", clientID)
            add("data", element)
        }
        return fcmApiHelper.sendTestMessage(json)
            .doOnError { Log.e(TAG, it.message) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(id: String, pw: String): Single<ServerResponse<Team>> {
        return apiHelper.login(id, pw)
            .map { response ->
                val userData = response.data[0]
                userData.run {
                    User(this.id, password, name, nickname, profileLink)
                }
            }
            .doOnSuccess {
                prefHelper.saveItem(PreferenceHelperImpl.CURRENT_USER_ID, it.id)
            }
            .flatMap { apiHelper.loadTeams(it.id) }
            .doOnSuccess {
                if(it.data.size!=0){
                    if(getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID).isEmpty())
                        prefHelper.saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID, it.data[0].id)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun logout(): Single<String> {
        return apiHelper.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun subscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            subscribeToTopic("main")
        }
    }

    override fun unSubscribeTopic(list: List<ChatRoom>) {
        FirebaseMessaging.getInstance().apply {
            unsubscribeFromTopic("main")
        }
    }

    override fun join(id: String, pw: String, name: String, nickname: String):Single<Int> {
        return apiHelper.join(id,pw,name,nickname,"")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createTeam(teamName: String): Single<String> {
        return apiHelper.createTeam(teamName)
            .doOnError { Log.e(TAG, it.message) }
            .doOnSuccess { dbHelper.insertTeam(Team(it, teamName)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadGroupClient(): Single<List<User>> {
        return apiHelper.loadGroupClient(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .map { response -> response.data }
            .doOnSuccess { dbHelper.updateUser(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun setNotice(text: String, chatRoomID: String): Single<Int> {
        return apiHelper.setNotice(text, chatRoomID)
            .doOnSuccess {
                Log.e(TAG, it.toString())
            }
            .doOnError {
                Log.e(TAG, it.toString())
            }
            .flatMap {
                apiHelper.loadNotice(chatRoomID)
            }
            .doOnSuccess {
                Log.e(TAG, it.toString())
            }
            .doOnError {
                Log.e(TAG, it.toString())
            }
            .flatMap {
                if (it.responseCode.toInt() == ErrorCode.SUCCESS.code) {
                    dbHelper.insertNotice(it.data[0])
                    Single.create<Int> {
                        it.onSuccess(ErrorCode.SUCCESS.code)
                    }
                } else {
                    Single.create<Int> {
                        it.onError(Throwable(ErrorCode.WRONG_PARAMETER.description))
                    }
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun loadNotice(chatRoomID: String): Single<Notice> {
        return apiHelper.loadNotice(chatRoomID)
            .map { response -> response.data[0] }
            .subscribeOn(Schedulers.io())
    }

    override fun createVote(vote: Vote, list: MutableList<String>): Observable<String> {
        val startDate = vote.startDate
        val endDate = vote.endDate
        val regex = Regex("(19|20)[0-9]{2}[.](0[1-9]|1[012])[.](0[1-9]|[12][0-9]|3[01])")
        if (!(regex.matches(startDate) && regex.matches(endDate))) {
            return Observable.create<String> {
                it.onError(Throwable(ErrorCode.NOT_PATTERN_MATCH.code.toString()))
            }
        }

        return apiHelper.createVote(
            vote.title,
            vote.startDate,
            vote.endDate,
            vote.duplicate,
            getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID)
        )
            .doOnSuccess {
                dbHelper.insertVote(
                    Vote(
                        it,
                        vote.title,
                        vote.startDate,
                        vote.endDate,
                        vote.duplicate,
                        getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID)
                    )
                )
            }
            .flatMapObservable {
                val voteID = it
                Log.e(TAG, it)
                Observable.create<String> { emitter ->
                    list.forEachIndexed { i, item ->
                        createVoteItem(item, voteID)
                            .subscribe({
                                if (list.size - 1 == i)
                                    emitter.onComplete()
                                if (it == ErrorCode.SUCCESS.code)
                                    emitter.onNext(ErrorCode.SUCCESS.code.toString())
                                else
                                    emitter.onError(Throwable(ErrorCode.WRONG_PARAMETER.code.toString()))
                            }, {
                                Log.e(TAG, it.message)
                            })
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createVoteItem(name: String, id: String): Single<Int> {
        return apiHelper.createVoteItem(name, id)
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.e(TAG, it.toString())
                dbHelper.insertVoteItem(VoteItem(it, name, 0, id))
            }
            .map { it ->
                if (it.isEmpty())
                    ErrorCode.WRONG_PARAMETER.code
                else
                    ErrorCode.SUCCESS.code
            }
    }

    override fun loadVote(): Single<List<Vote>> {
        return apiHelper.loadVotes(getItem(PreferenceHelperImpl.CURRENT_CHAT_ROOM_ID))
            .subscribeOn(Schedulers.io())
            .map { response -> response.data }
            .doOnSuccess { dbHelper.insertVoteList(it) }
    }

    override fun loadDetailVote(voteID: String): Single<Vote> {
        val checkVote = apiHelper.checkVote(voteID)
            .subscribeOn(Schedulers.io())
            .map { response -> response.data }
            .map { list ->
                val user = list.find { it.id == getItem(PreferenceHelperImpl.CURRENT_USER_ID) }
                user != null
            }

        return apiHelper.loadDetailVote(voteID)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { Log.e(TAG, it.toString()) }
            .map { response -> response.data }
            .zipWith(checkVote, BiFunction { data: VoteData, check: Boolean ->
                val vote = data.vote
                if (check) {
                    vote.itemList.addAll(data.voteList)
                    vote.select = 1
                    vote
                } else {
                    vote.itemList.addAll(data.voteList)
                    vote
                }
            })
    }

    override fun acceptVote(voteID: String, voteItemIDlist: List<String>): Observable<ErrorCode> {
        return Observable.create<ErrorCode> { emitter ->
            voteItemIDlist.forEachIndexed { index, s ->
                vote(voteID, s)
                    .subscribe({
                        if (voteItemIDlist.size - 1 == index && it == ErrorCode.SUCCESS){
                            emitter.onNext(it)
                            emitter.onComplete()
                        }
                        if (it == ErrorCode.SUCCESS)
                            emitter.onNext(it)
                        else
                            emitter.onError(Throwable(it.description))
                    }, {
                        Log.e(TAG, it.message)
                    })
            }
        }
    }

    private fun vote(voteID: String, voteItemID: String): Single<ErrorCode> {
        return apiHelper.vote(voteID, voteItemID, getItem(PreferenceHelperImpl.CURRENT_USER_ID))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> ErrorCode.fromCode(response) }
    }

    override fun uploadFile(path: String): Single<Int> {
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val teamID = prefHelper.getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        val requestBody = RequestBody.create(MediaType.parse("text/plane"), teamID)
        val body = MultipartBody.Part.createFormData("file", URLEncoder.encode(file.name, "utf-8"), requestFile)

        return apiHelper.uploadFile(requestBody, body)
            .map { response -> response.responseCode.toInt() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadReferences(): Observable<ServerResponse<Reference>> {
        return apiHelper.loadReferences(prefHelper.getItem(PreferenceHelperImpl.CURRENT_GROUP_ID))
            .doOnError {
                Log.e(TAG, it.message)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    override fun uploadFile(list: MutableList<MultipartBody.Part>): Observable<ServerResponse<com.example.myapplication.data.model.File>> =
        Observable.fromIterable(list)
            .flatMapSingle {
                apiHelper.uploadReferences(getItem(PreferenceHelperImpl.CURRENT_GROUP_ID), it)
            }.subscribeOn(Schedulers.io())

    override fun downloadFile(fileName: String): Single<File> =
        apiHelper.downloadReference(fileName)
            .flatMap {

                val file =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "catsix" + File.separator, fileName)

                if(!file.exists()) {
                    file.parentFile.mkdirs()
                }

                val sink = Okio.buffer(Okio.sink(file))
                it.body()!!.apply {
                    sink.writeAll(source())
                    close()
                }

                Single.just(file)
            }.doOnError { Log.d("ZZZ", "Fail") }
            .doOnSuccess { Log.d("ZZZ", "SUCCESS") }
            .subscribeOn(Schedulers.io())
}