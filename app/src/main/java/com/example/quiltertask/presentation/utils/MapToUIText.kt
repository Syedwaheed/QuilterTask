package com.example.quiltertask.presentation.utils

import com.example.quiltertask.R
import com.example.quiltertask.data.utils.DataError

fun DataError.asUiText(): UIText {
    return when (this) {
        DataError.Network.BAD_REQUEST -> UIText.StringResource(R.string.bad_request)
        DataError.Network.NOT_FOUND -> UIText.StringResource(R.string.not_found)
        DataError.Network.REQUEST_TIMEOUT -> UIText.StringResource(R.string.the_request_timed_out)
        DataError.Network.SERVER_ERROR -> UIText.StringResource(R.string.server_error)
        DataError.Network.NO_INTERNET -> UIText.StringResource(R.string.no_internet)
        DataError.Network.TOO_MANY_REQUEST -> UIText.StringResource(R.string.too_many)
        DataError.Network.SERIALIZATION -> UIText.StringResource(R.string.error_serialization)
        DataError.Network.PAYLOAD_TOO_LARGE -> UIText.StringResource(R.string.pay_load_large)
        DataError.Network.UNKNOWN_ERROR -> UIText.StringResource(R.string.unknown_error)
        DataError.Network.UN_AUTHORIZE -> UIText.StringResource(R.string.un_auth)
    }
}