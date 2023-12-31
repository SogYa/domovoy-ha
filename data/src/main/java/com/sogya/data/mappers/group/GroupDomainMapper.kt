package com.sogya.data.mappers.group

import com.sogya.data.models.StateGroup
import com.sogya.domain.models.StateGroupDomain

class GroupDomainMapper(
    private val groupDomain: StateGroupDomain
) {
   fun toGroupData() = StateGroup(
       groupDomain.groupId,
       groupDomain.ownerId,
       groupDomain.groupTag,
       groupDomain.groupDesc
   )
}