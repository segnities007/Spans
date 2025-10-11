package com.segnities007.usecase.encounter

import com.segnities007.model.Encounter
import com.segnities007.repository.EncounterRepository

class GetEncountersUseCase(
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(): Result<List<Encounter>> {
        // 早期リターン: すれ違い情報取得失敗
        val encountersResult = encounterRepository.getEncounters()
        if (encountersResult.isFailure) {
            return encountersResult
        }

        val encounters = encountersResult.getOrNull()

        // 早期リターン: すれ違いがない
        if (encounters.isNullOrEmpty()) {
            return Result.success(emptyList())
        }

        // ユーザー情報で補完
        val enrichedResult = encounterRepository.enrichEncountersWithUserInfo(encounters)

        // 失敗した場合は元のデータを返す
        return if (enrichedResult.isFailure) {
            Result.success(encounters)
        } else {
            enrichedResult
        }
    }
}
