package ru.sherb.igorprj.endpoint

import org.springframework.data.domain.Page

/**
 * @author maksim
 * @since 10.09.2019
 */
data class PageView<T>(
        val content: List<T>,
        val hasNext: Boolean,
        val hasPrev: Boolean
) {
    constructor(page: Page<T>): this(page.content, page.hasNext(), page.hasPrevious())
}

fun <T> emptyPageView() = PageView<T>(emptyList(), false, false)