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

        // すれ違い時刻のマップを作成
        // 重複ユーザーがいる場合は最新の lastEncounterAt を保持
        val encounterTimestamps = buildMap<String, Long> {
            encounters.forEach { encounter ->
                // userUuidA の最大タイムスタンプを保持
                merge(encounter.userUuidA, encounter.lastEncounterAt) { existing, new ->
                    maxOf(existing, new)
                }
                // userUuidB の最大タイムスタンプを保持
                merge(encounter.userUuidB, encounter.lastEncounterAt) { existing, new ->
                    maxOf(existing, new)
                }
            }
        }
        
        // すれ違ったユーザーのUUIDリスト（重複なし）
        // TODO: 将来的に自分のUUIDを除外する
        val encounterUuids = encounterTimestamps.keys.toList()

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
