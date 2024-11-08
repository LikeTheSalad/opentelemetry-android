package io.opentelemetry.android.demo.globalattrs

import io.opentelemetry.api.common.Attributes
import java.util.function.Supplier

interface GlobalAttributeSupplier : Supplier<Attributes> {
    fun set(attributes: Attributes)
}