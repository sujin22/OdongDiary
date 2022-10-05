package com.example.owndiary.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.owndiary.model.Diary
import kotlinx.coroutines.flow.Flow

//@Dao
//interface DiaryDao {
//    @Insert
//    fun insert(diary: Diary)
//
//    @Update
//    fun update(diary: Diary)
//
//    @Delete
//    fun delete(diary: Diary)
//
//    //테이블의 모든 값 불러오는 메소드
//    @Query("SELECT * FROM diary_table")
//    fun getAll(): Flow<List<Diary>>
//
//    @Query("DELETE FROM diary_table")
//    fun deleteAll()
//
//    /*TODO
//    * - 날짜 오름차순, 내림차순 정렬 반환 쿼리
//    * - favorite인 Data만 가져오는 쿼리
//    * */
//
//}