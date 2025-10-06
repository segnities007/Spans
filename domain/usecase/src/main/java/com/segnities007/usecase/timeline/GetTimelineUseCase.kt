package com.segnities007.usecase.timeline

import com.segnities007.model.Post
import com.segnities007.repository.EncounterRepository
import com.segnities007.repository.PostRepository

class GetTimelineUseCase(
    private val postRepository: PostRepository,
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(
        offset: Int = 0,
        limit: Int = 20
    ): Result<List<Post>> {
        // 早期リターン: すれ違い情報取得失敗
        val encountersResult = encounterRepository.getEncounters()
        if (encountersResult.isFailure) {
            return Result.failure(
                encountersResult.exceptionOrNull() ?: Exception("すれ違い情報の取得に失敗しました")
            )
        }

        val encounters = encountersResult.getOrNull()

        // 早期リターン: すれ違いがない
        if (encounters.isNullOrEmpty()) {
            return Result.success(emptyList())
        }

        // すれ違ったユーザーのUUIDリストを作成
        val encounterUuids = encounters.map { encounter ->
            // 自分のUUIDは除外する必要があるが、ここでは全て含める
            listOf(encounter.userUuidA, encounter.userUuidB)
        }.flatten().distinct()

        // すれ違い時刻のマップを作成
        val encounterTimestamps = encounters.associate { encounter ->
            encounter.userUuidA to encounter.lastEncounterAt
        } + encounters.associate { encounter ->
            encounter.userUuidB to encounter.lastEncounterAt
        }

        // 早期リターン: タイムライン取得失敗
        val timelineResult = postRepository.getTimeline(
            encounterUuids = encounterUuids,
            encounterTimestamps = encounterTimestamps,
            limit = limit,
            offset = offset
        )

        if (timelineResult.isFailure) {
            return Result.failure(
                timelineResult.exceptionOrNull() ?: Exception("タイムラインの取得に失敗しました")
            )
        }

        return timelineResult
    }
}
