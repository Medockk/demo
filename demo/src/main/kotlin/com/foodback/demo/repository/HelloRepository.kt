package com.foodback.demo.repository

import com.foodback.demo.model.HelloModelEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HelloRepository: JpaRepository<HelloModelEntity, Long> {

}