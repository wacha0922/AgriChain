package com.example.agrichain.data.util

/**
 * Detects the broad agricultural category of a crop/product
 * based on the name entered by the farmer.
 *
 * This is intentionally lightweight and deterministic.
 *
 * Examples:
 *
 * Tomato           -> Vegetables
 * Alphonso Mango   -> Fruits
 * Basmati Rice     -> Grains
 * Toor Dal         -> Pulses
 * Turmeric         -> Spices
 * Foxtail Millet   -> Millets
 * Groundnut        -> Oilseeds
 *
 * The detector returns null when it cannot confidently
 * identify the category.
 */
object CropCategoryDetector {

    private val vegetables = listOf(
        "tomato",
        "potato",
        "onion",
        "spinach",
        "carrot",
        "cabbage",
        "cauliflower",
        "brinjal",
        "eggplant",
        "okra",
        "ladyfinger",
        "peas",
        "green peas",
        "cucumber",
        "capsicum",
        "bell pepper",
        "beetroot",
        "radish",
        "turnip",
        "pumpkin",
        "bottle gourd",
        "lauki",
        "bitter gourd",
        "ridge gourd",
        "snake gourd",
        "drumstick",
        "broccoli",
        "lettuce",
        "beans",
        "french beans",
        "sweet corn"
    )

    private val fruits = listOf(
        "mango",
        "alphonso",
        "banana",
        "apple",
        "papaya",
        "orange",
        "grape",
        "grapes",
        "guava",
        "pomegranate",
        "watermelon",
        "pineapple",
        "muskmelon",
        "melon",
        "strawberry",
        "chikoo",
        "sapota",
        "custard apple",
        "dragon fruit",
        "kiwi",
        "lemon",
        "lime"
    )

    private val grains = listOf(
        "rice",
        "basmati",
        "wheat",
        "maize",
        "corn",
        "barley",
        "sorghum",
        "oat",
        "oats",
        "rye",
        "brown rice",
        "paddy"
    )

    private val pulses = listOf(
        "toor",
        "toor dal",
        "pigeon pea",
        "arhar",
        "moong",
        "moong dal",
        "green gram",
        "urad",
        "urad dal",
        "black gram",
        "masoor",
        "masoor dal",
        "lentil",
        "chickpea",
        "chick peas",
        "gram",
        "chana",
        "kabuli chana",
        "rajma",
        "kidney bean",
        "cowpea",
        "lobia"
    )

    private val spices = listOf(
        "turmeric",
        "haldi",
        "cumin",
        "jeera",
        "chilli",
        "chili",
        "red chilli",
        "red chili",
        "black pepper",
        "pepper",
        "cardamom",
        "clove",
        "cinnamon",
        "nutmeg",
        "mace",
        "coriander",
        "coriander seed",
        "fenugreek",
        "methi",
        "fennel",
        "saunf",
        "mustard spice"
    )

    private val millets = listOf(
        "millet",
        "millets",
        "ragi",
        "finger millet",
        "bajra",
        "pearl millet",
        "jowar",
        "foxtail millet",
        "foxtail",
        "little millet",
        "kodo millet",
        "barnyard millet",
        "proso millet",
        "sama",
        "barnyard"
    )

    private val oilseeds = listOf(
        "groundnut",
        "peanut",
        "soybean",
        "soya bean",
        "soya",
        "sunflower",
        "sesame",
        "til",
        "mustard seed",
        "mustard",
        "safflower",
        "castor",
        "linseed",
        "flaxseed",
        "rapeseed"
    )

    /**
     * Attempts to identify an agricultural category.
     *
     * Returns null when no category confidently matches.
     */
    fun detect(
        cropName: String
    ): String? {

        val normalized =
            normalize(
                cropName
            )

        if (normalized.isBlank()) {
            return null
        }

        /*
         * Order matters.
         *
         * More specific multi-word terms are handled first
         * through contains matching against normalized names.
         */

        return when {

            matches(
                normalized,
                vegetables
            ) ->
                "Vegetables"

            matches(
                normalized,
                fruits
            ) ->
                "Fruits"

            matches(
                normalized,
                pulses
            ) ->
                "Pulses"

            matches(
                normalized,
                spices
            ) ->
                "Spices"

            matches(
                normalized,
                millets
            ) ->
                "Millets"

            matches(
                normalized,
                oilseeds
            ) ->
                "Oilseeds"

            matches(
                normalized,
                grains
            ) ->
                "Grains"

            else ->
                null
        }
    }

    /**
     * Normalizes user input so that:
     *
     * "Organic Tomatoes"
     * "organic tomato"
     * "TOMATO"
     *
     * can all be recognized consistently.
     */
    private fun normalize(
        value: String
    ): String {

        return value
            .lowercase()
            .replace(
                Regex("[^a-z0-9\\s]"),
                " "
            )
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()
    }

    /**
     * Checks whether a normalized crop name contains
     * one of the known agricultural terms.
     */
    private fun matches(
        normalized: String,
        keywords: List<String>
    ): Boolean {

        return keywords.any { keyword ->

            normalized.contains(
                keyword
            )
        }
    }
}