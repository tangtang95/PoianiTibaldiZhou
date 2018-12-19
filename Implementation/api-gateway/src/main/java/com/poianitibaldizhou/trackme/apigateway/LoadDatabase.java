package com.poianitibaldizhou.trackme.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    /*
    @Bean
    CommandLineRunner initDatabase(UserRepository repository, ThirdPartyRepository thirdPartyRepository,
                                   CompanyDetailRepository companyDetailRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User();
            user.setSsn("user1");
            user.setUsername("username1");
            user.setPassword(passwordEncoder.encode("password1"));
            user.setBirthNation("Italy");
            user.setBirthDate(new Date(0));
            user.setLastName("cordero");
            user.setFirstName("mattia");
            user.setBirthCity("China");

            ThirdPartyCustomer thirdPartyCustomer = new ThirdPartyCustomer();
            thirdPartyCustomer.setEmail("email1");
            thirdPartyCustomer.setPassword(passwordEncoder.encode("passwordtp"));

            CompanyDetail companyDetail = new CompanyDetail();
            companyDetail.setAddress("addresstp");
            companyDetail.setDunsNumber("dunsssss");
            companyDetail.setCompanyName("polimi");
            companyDetail.setThirdPartyCustomer(thirdPartyCustomer);


            log.info("Preloading " + repository.saveAndFlush(user));
            log.info("Preloading " + companyDetailRepository.saveAndFlush(companyDetail));
        };
    }
    */
}
