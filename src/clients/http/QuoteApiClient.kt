package com.example.clients.http

import com.example.model.Quote

interface QuoteApiClient {
    suspend fun getQuote(): Quote
}