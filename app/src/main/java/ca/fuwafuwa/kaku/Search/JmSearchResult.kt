package ca.fuwafuwa.kaku.Search

import ca.fuwafuwa.kaku.Database.JmDictDatabase.Models.EntryOptimized
import ca.fuwafuwa.kaku.Database.JmDictDatabase.Models.PitchOptimized
import ca.fuwafuwa.kaku.Deinflictor.DeinflectionInfo
import com.mariten.kanatools.KanaConverter

data class JmSearchResult(
        val entry: EntryOptimized,
        val deinfInfo: DeinflectionInfo,
        val word: String,
        val pitches: List<PitchOptimized>
) {
    fun getPitchForReadingOrNull(reading: String) : Int? {
        // TODO: Possibly a waste of time to do conversions
        val convOpFlags = KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII or KanaConverter.OP_HAN_KATA_TO_ZEN_HIRA or KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA
        val readingConverted = KanaConverter.convertKana(reading, convOpFlags)

        return pitches.find {
            val pitchReadingConverted = KanaConverter.convertKana(it.reading, convOpFlags)
            pitchReadingConverted == readingConverted
        }?.pitch
    }
}