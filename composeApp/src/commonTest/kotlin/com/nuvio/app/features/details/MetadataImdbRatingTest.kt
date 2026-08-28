package com.nuvio.app.features.details

import com.nuvio.app.features.details.components.asDisplayableImdbRating
import com.nuvio.app.features.details.components.metadataImdbRatingToDisplay
import com.nuvio.app.features.mdblist.MdbListMetadataService.PROVIDER_IMDB
import com.nuvio.app.features.mdblist.MdbListMetadataService.PROVIDER_TOMATOES
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MetadataImdbRatingTest {
    private fun meta(
        imdbRating: String? = null,
        externalRatings: List<MetaExternalRating> = emptyList(),
    ) = MetaDetails(
        id = "tt0111161",
        type = "movie",
        name = "The Shawshank Redemption",
        imdbRating = imdbRating,
        externalRatings = externalRatings,
    )

    @Test
    fun `shows the rating that came with the metadata`() {
        assertEquals("9.3", meta(imdbRating = "9.3").metadataImdbRatingToDisplay())
    }

    @Test
    fun `hides it when MDBList already supplies an IMDb rating`() {
        val result = meta(
            imdbRating = "9.3",
            externalRatings = listOf(MetaExternalRating(source = PROVIDER_IMDB, value = 9.3)),
        ).metadataImdbRatingToDisplay()

        assertNull(result)
    }

    @Test
    fun `still shows it when MDBList supplies other providers only`() {
        val result = meta(
            imdbRating = "9.3",
            externalRatings = listOf(MetaExternalRating(source = PROVIDER_TOMATOES, value = 91.0)),
        ).metadataImdbRatingToDisplay()

        assertEquals("9.3", result)
    }

    @Test
    fun `the shared validity check accepts and rejects the same values everywhere`() {
        // The hover preview has no MDBList data, so it uses this half directly.
        assertEquals("8.2", "8.2".asDisplayableImdbRating())
        assertEquals("8.2", " 8.2 ".asDisplayableImdbRating())
        assertNull(null.asDisplayableImdbRating())
        assertNull("".asDisplayableImdbRating())
        assertNull("N/A".asDisplayableImdbRating())
        assertNull("0".asDisplayableImdbRating())
    }

    @Test
    fun `hides missing, unparseable and zero ratings`() {
        assertNull(meta(imdbRating = null).metadataImdbRatingToDisplay())
        assertNull(meta(imdbRating = "").metadataImdbRatingToDisplay())
        assertNull(meta(imdbRating = "N/A").metadataImdbRatingToDisplay())
        assertNull(meta(imdbRating = "0.0").metadataImdbRatingToDisplay())
    }
}
