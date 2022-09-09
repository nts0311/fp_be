package com.sonnt.fp_be.features.enduser.home.response

import com.sonnt.fp_be.features.enduser.home.model.HomeSection
import com.sonnt.fp_be.model.dto.response.BaseResponse

class GetHomeSectionResponse(
    var sections: List<HomeSection> = listOf()
): BaseResponse()