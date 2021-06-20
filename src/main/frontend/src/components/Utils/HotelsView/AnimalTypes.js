import i18n from "i18next";
export const animalTypes = [
    {
        value: 1,
        label: i18n.t('DOG')
    },
    {
        value: 2,
        label: i18n.t('CAT')
    },
    {
        value: 3,
        label: i18n.t('RODENT')
    },
    {
        value: 4,
        label: i18n.t('BIRD')
    },
    {
        value: 5,
        label: i18n.t('RABBIT')
    },
    {
        value: 6,
        label: i18n.t('LIZARD')
    },
    {
        value: 7,
        label: i18n.t('TURTLE')
    }
]

export function queryBuilder(minVal, maxVal, animalTypes, searchText){
    let query = '?'
    if (minVal !== undefined) query += '&fromRating=' + minVal
    if (maxVal !== undefined) query += '&toRating=' + maxVal
    if (animalTypes.indexOf(1)> -1) query += '&dogType=true'
    if (animalTypes.indexOf(2)> -1) query += '&catType=true'
    if (animalTypes.indexOf(3)> -1) query += '&rodentType=true'
    if (animalTypes.indexOf(4)> -1) query += '&birdType=true'
    if (animalTypes.indexOf(5)> -1) query += '&rabbitType=true'
    if (animalTypes.indexOf(6)> -1) query += '&lizardType=true'
    if (animalTypes.indexOf(7)> -1) query += '&turtleType=true'
    query += '&searchQuery=' + searchText

    return query
}