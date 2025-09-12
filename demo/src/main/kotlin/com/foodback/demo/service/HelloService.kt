package com.foodback.demo.service

import com.foodback.demo.model.HelloModelDto
import com.foodback.demo.repository.HelloRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class HelloService(
    private val helloRepository: HelloRepository
) {

    fun getHello(search: String?): List<HelloModelDto> {
        return if (search != null) {
            helloRepository
                .findAll()
                .filter { it.content.contains(search, true) }
                .map { it.toDto() }
        } else {
            helloRepository
                .findAll()
                .map { it.toDto() }
        }
    }

    fun getHelloById(id: Long): HelloModelDto? {
        return helloRepository.findByIdOrNull(id)?.toDto()
    }

    fun postHello(helloModelDto: HelloModelDto): HelloModelDto {
        return helloRepository.save(
            helloModelDto.toEntity()
                .apply {
                    id = 0
                }
        ).toDto()
    }

    fun putHello(id: Long, helloModelDto: HelloModelDto): HelloModelDto {
        val entity = helloRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Oops, this data not found"
        )

        return helloRepository.save(entity.apply {
            this.content = helloModelDto.content
            this.createdAt = helloModelDto.createdAt
        }).toDto()
    }

    fun deleteHello(id: Long) {
        helloRepository.deleteById(id)
    }
}