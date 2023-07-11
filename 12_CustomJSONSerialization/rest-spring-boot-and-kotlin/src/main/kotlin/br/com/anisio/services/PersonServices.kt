package br.com.anisio.services

import br.com.anisio.PersonRepository
import br.com.anisio.data.vo.v1.PersonVO
import br.com.anisio.data.vo.v2.PersonVO as PersonVOV2
import br.com.anisio.exceptions.ResourceNotFoundException
import br.com.anisio.mapper.DozerMapper
import br.com.anisio.mapper.custom.PersonMapper
import br.com.anisio.model.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonServices {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people!")
        val persons = repository.findAll()
        return DozerMapper.parseListObjects(persons, PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one person!")
        val person = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        return DozerMapper.parseObject(person, PersonVO::class.java)
    }

    fun create(person: PersonVO) : PersonVO  {
        logger.info("Create one person with name ${person.firstName}!")
        val entity = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }

//    fun createV2(person: PersonVOV2) : PersonVOV2 {
//        logger.info("Create one person with name ${person.firstName}!")
//        val entity = mapper.mapVOToEntity(person)
//        return mapper.mapEntityToVO(repository.save(entity))
//    }

    fun update(person: PersonVO) : PersonVO {
        logger.info("Update one person with ID ${person.id}!")

        val entity = repository.findById(person.id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

    }

    fun delete(id: Long) {
        logger.info("Delete one person with ID $id!")
        val entity = repository.findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this ID!")}
        repository.delete(entity)
    }

}
