package com.example.model

data class PostSnippet(val snippet: PostSnippet.Text) {
    data class Text(val text: String)
}