package com.fx.santander.exceptions

class NotFoundException(id: String) : RuntimeException("Could not find for: $id")