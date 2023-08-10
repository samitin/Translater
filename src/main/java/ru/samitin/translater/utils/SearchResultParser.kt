package ru.samitin.translater.utils


import ru.samitin.model.SearchResultDto
import ru.samitin.model.MeaningsDto
import ru.samitin.model.entity.DataModel
import ru.samitin.model.entity.Meaning
import ru.samitin.model.entity.TranslatedMeaning
import ru.samitin.model.state.AppState


// Все методы говорят сами за себя, универсальны и парсят данные в зависимости
// от источника данных (интернет или БД), возвращая их в понятном для наших
// Activity форматах
fun mapSearchResultToResult(searchResults: List<SearchResultDto>): List<DataModel> {
    return searchResults.map { searchResult ->
        var meanings: List<Meaning> = listOf()
        searchResult.meanings?.let {
//Check for null for HistoryScreen
            meanings = it.map { meaningsDto ->
                Meaning(
                    TranslatedMeaning(meaningsDto?.translation?.translation ?: ""),
                    meaningsDto?.imageUrl ?: ""
                )
            }
        }
        DataModel(searchResult.text ?: "", meanings)
    }
}
fun parseOnlineSearchResults(data: AppState): AppState {
    return AppState.Success(mapResult(data, true))
}
private fun mapResult(
    data: AppState,
    isOnline: Boolean
): List<DataModel> {
    val newSearchResults = arrayListOf<DataModel>()
    when (data) {
        is AppState.Success -> {
            getSuccessResultData(data, isOnline, newSearchResults)
        }

        else -> {}
    }
    return newSearchResults
}
private fun getSuccessResultData(
    data: AppState.Success,
    isOnline: Boolean,
    newSearchDataModels: ArrayList<DataModel>
) {
    val searchDataModels: List<DataModel> = data.data as List<DataModel>
    if (searchDataModels.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in searchDataModels) {
                parseOnlineResult(searchResult, newSearchDataModels)
            }
        } else {
            for (searchResult in searchDataModels) {
                newSearchDataModels.add(
                    DataModel(
                        searchResult.text,
                        arrayListOf()
                    )
                )
            }
        }
    }
}
private fun parseOnlineResult(
    searchDataModel: DataModel,
    newSearchDataModels: ArrayList<DataModel>
) {
    if (searchDataModel.text.isNotBlank() && searchDataModel.meanings.isNotEmpty()) {
        val newMeanings = arrayListOf<Meaning>()
        newMeanings.addAll(searchDataModel.meanings.filter {
            it.translatedMeaning.translatedMeaning.isNotBlank() })
        if (newMeanings.isNotEmpty()) {
            newSearchDataModels.add(
                DataModel(
                    searchDataModel.text,
                    newMeanings
                )
            )
        }
    }
}

fun convertMeaningsToSingleString(meanings: List<Meaning>): String {
    var meaningsSeparatedByComma = String()
    for ((index, meaning) in meanings.withIndex()) {
        meaningsSeparatedByComma += if (index + 1 != meanings.size) {
            String.format("%s%s", meaning.translatedMeaning.translatedMeaning, ", ")
        } else {
            meaning.translatedMeaning.translatedMeaning
        }
    }
    return meaningsSeparatedByComma
}