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
        return encounterRepository.getEncounters().fold(
            onSuccess = { encounters ->
                // 早期リターン: すれ違いがない
                if (encounters.isEmpty()) {
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

                // タイムライン取得
                postRepository.getTimeline(
                    encounterUuids = encounterUuids,
                    encounterTimestamps = encounterTimestamps,
                    limit = limit,
                    offset = offset
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
