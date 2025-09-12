package com.foodback.demo.controller

import com.foodback.demo.model.HelloModelDto
import com.foodback.demo.service.HelloService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloWorldController(
    private val helloService: HelloService
) {

    @GetMapping
    fun getHello(
        @RequestParam("s", required = false) search: String?
    ): List<HelloModelDto> {
        return helloService.getHello(search)
    }

    @GetMapping("/id")
    fun getHelloById(
        @RequestParam("id", required = true) id: Long
    ): HelloModelDto? {
        return helloService.getHelloById(id)
    }

    @PostMapping
    fun postHello(
        @RequestBody helloModelDto: HelloModelDto
    ): HelloModelDto {
        return helloService.postHello(helloModelDto)
    }

    @PutMapping
    fun putHello(
        @RequestParam id: Long,
        @RequestBody helloModelDto: HelloModelDto
    ): HelloModelDto {
        return helloService.putHello(id, helloModelDto)
    }

    @DeleteMapping
    fun deleteHello(
        @RequestParam id: Long
    ) {
        helloService.deleteHello(id)
    }
}