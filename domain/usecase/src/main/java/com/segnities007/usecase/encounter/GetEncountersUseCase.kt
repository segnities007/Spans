package com.segnities007.usecase.encounter

import com.segnities007.model.Encounter
import com.segnities007.repository.EncounterRepository

class GetEncountersUseCase(
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(): Result<List<Encounter>> {
        return encounterRepository.getEncounters().fold(
            onSuccess = { encounters ->
                // 早期リターン: すれ違いがない
                if (encounters.isEmpty()) {
                    return Result.success(emptyList())
                }

                // ユーザー情報で補完（失敗した場合は元のデータを返す）
                encounterRepository.enrichEncountersWithUserInfo(encounters).fold(
                    onSuccess = { enriched -> Result.success(enriched) },
                    onFailure = { Result.success(encounters) }
                )
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}
