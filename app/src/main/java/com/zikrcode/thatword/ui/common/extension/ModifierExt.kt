package com.zikrcode.thatword.ui.common.extension

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import com.zikrcode.thatword.utils.Dimens

fun Modifier.appClipRoundedCorner() =
    this.clip(RoundedCornerShape(Dimens.SpacingSingleHalf))

fun Modifier.appClipCircle() =
    this.clip(CircleShape)

fun Modifier.appShadowElevation(shape: Shape) =
    this.shadow(
        elevation = Dimens.ElevationSingleHalf,
        shape = shape
    )