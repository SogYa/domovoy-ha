package com.sogya.data.mappers.state.attributes

import com.sogya.data.models.Attributes
import com.sogya.domain.models.AttributesDomain

class AttributeDomainMapper(
    private val attribute: AttributesDomain
) {
    fun toAttributeData() = Attributes(
        attribute.entityIds,
        attribute.nextDawn,
        attribute.nextDusk,
        attribute.nextMidnight,
        attribute.nextNoon,
        attribute.nextRising,
        attribute.nextSetting,
        attribute.fanSpeedList,
        attribute.speedList,
        attribute.assumedState,
        attribute.friendlyName,
        attribute.appName,
        attribute.isVolumeMuted,
        attribute.volumeLevel,
        attribute.entityPicture,
        attribute.icon,
        attribute.title,
        attribute.year,
        attribute.month,
        attribute.day,
        attribute.hour,
        attribute.minute,
        attribute.timestamp,
        attribute.hasDate,
        attribute.hasTime,
        attribute.deviceClass,
        attribute.brightness,
        attribute.colorTemp,
        attribute.rgbColors,
        attribute.options,
        attribute.order,
        attribute.auto,
        attribute.hidden,
        attribute.view,
        attribute.name,
        attribute.initial,
        attribute.codeFormat,
        attribute.pattern,
        attribute.max,
        attribute.min,
        attribute.step,
        attribute.currentTemperature,
        attribute.maxTemp,
        attribute.minTemp,
        attribute.temperature,
        attribute.operationMode,
        attribute.activity,
        attribute.provider,
        attribute.sourceType,
        attribute.battery,
        attribute.gpsAccuracy,
        attribute.altitude,
        attribute.latitude,
        attribute.longitude,
        attribute.radius,
        attribute.releaseNotes,
        attribute.unitOfMeasurement,
    )
}