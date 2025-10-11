package com.segnities007.usecase.encounter

import com.segnities007.repository.EncounterRepository
import com.segnities007.repository.EncounterStats

class GetEncounterStatsUseCase(
    private val encounterRepository: EncounterRepository
) {
    suspend operator fun invoke(): Result<EncounterStats> {
        return encounterRepository.getEncounterStats()
    }
}
