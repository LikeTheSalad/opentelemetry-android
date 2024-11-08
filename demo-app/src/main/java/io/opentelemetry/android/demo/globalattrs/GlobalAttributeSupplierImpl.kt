package io.opentelemetry.android.demo.globalattrs

import io.opentelemetry.api.common.Attributes

class GlobalAttributeSupplierImpl : GlobalAttributeSupplier {
    @Volatile
    private var attributes: Attributes = Attributes.empty()

    override fun set(attributes: Attributes) {
        this.attributes = attributes
    }

    override fun get(): Attributes = attributes

    fun createInitialAttributesWithKey(keys: List<String>) {
        attributes = Attributes.builder()
            .putAll(mapToAttrs(keys.associateWith { "unknown" })) // I replaced the empty string with "unknown" because Elastic discards empty attrs.
            .build()
    }

    fun update(key: String, value: String) {
        val tempAttributes: MutableMap<String, Any> = mutableMapOf()
        attributes.forEach { k, v ->
            tempAttributes[k.key] = v
        }
        tempAttributes[key] = value
        set(
            Attributes.builder()
                .putAll(mapToAttrs(tempAttributes))
                .build()
        )
    }

    private fun mapToAttrs(map: Map<String, Any>): Attributes {
        val builder = Attributes.builder()

        map.forEach { (key, value) ->
            when (value) {
                is String -> builder.put(key, value)
                is Long -> builder.put(key, value)
                is Double -> builder.put(key, value)
                is Boolean -> builder.put(key, value)
                is Int -> builder.put(key, value.toLong())
                else -> throw UnsupportedOperationException()
            }
        }

        return builder.build()
    }
}