package com.example.demo;

import com.example.demo.repository.custom.GenericSearchRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//  cho phép mở rộng chức năng mặc định của Spring Data JPA repositories
//  bằng cách thêm các phương thức tùy chỉnh (như searchByField)
//  vào tất cả các repositories nếu nó extends từ GenericSearchRepository


@EnableJpaRepositories(repositoryBaseClass = GenericSearchRepositoryImpl.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
