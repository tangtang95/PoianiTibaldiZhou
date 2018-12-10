package com.poianitibaldizhou.trackme.accountservice.service;

import com.poianitibaldizhou.trackme.accountservice.entity.CompanyDetail;
import com.poianitibaldizhou.trackme.accountservice.entity.PrivateThirdPartyDetail;
import com.poianitibaldizhou.trackme.accountservice.entity.ThirdPartyCustomer;
import com.poianitibaldizhou.trackme.accountservice.exception.AlreadyPresentEmailException;
import com.poianitibaldizhou.trackme.accountservice.exception.ThirdPartyCustomerNotFoundException;
import com.poianitibaldizhou.trackme.accountservice.repository.CompanyDetailRepository;
import com.poianitibaldizhou.trackme.accountservice.repository.PrivateThirdPartyDetailRepository;
import com.poianitibaldizhou.trackme.accountservice.repository.ThirdPartyRepository;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyCompanyWrapper;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyPrivateWrapper;
import com.poianitibaldizhou.trackme.accountservice.util.ThirdPartyWrapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the services regarding the management of third party accounts
 */
@Service
public class ThirdPartyAccountManagerServiceImpl implements ThirdPartyAccountManagerService {

    private ThirdPartyRepository thirdPartyRepository;
    private CompanyDetailRepository companyDetailRepository;
    private PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository;

    /**
     * Creates the manager of the services regarding the accounts of third party customers.
     * It needs some repositories in order to make some operations on data (e.g. register a user)
     *
     *
     * @param thirdPartyRepository repository regarding general information of third party customers
     * @param companyDetailRepository repository regarding companies associated with third party customers
     * @param privateThirdPartyDetailRepository repository regarding third party customers that are not related
     *                                          with companies
     */
    public ThirdPartyAccountManagerServiceImpl(ThirdPartyRepository thirdPartyRepository,
                                               CompanyDetailRepository companyDetailRepository,
                                               PrivateThirdPartyDetailRepository privateThirdPartyDetailRepository) {
        this.thirdPartyRepository = thirdPartyRepository;
        this.companyDetailRepository = companyDetailRepository;
        this.privateThirdPartyDetailRepository = privateThirdPartyDetailRepository;

    }

    @Override
    public Optional<ThirdPartyCompanyWrapper> getThirdPartyCompanyByEmail(String email) {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findByEmail(email).orElseThrow(() -> new ThirdPartyCustomerNotFoundException(email));

        Optional<CompanyDetail> companyDetail = companyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer);

        if(companyDetail.isPresent()) {
            ThirdPartyCompanyWrapper wrapper = new ThirdPartyCompanyWrapper();
            wrapper.setThirdPartyCustomer(thirdPartyCustomer);
            wrapper.setCompanyDetail(companyDetail.get());
            return Optional.of(wrapper);
        }

        return Optional.empty();
    }

    @Override
    public Optional<ThirdPartyPrivateWrapper> getThirdPartyPrivateByEmail(String email) {
        ThirdPartyCustomer thirdPartyCustomer = thirdPartyRepository.findByEmail(email).orElseThrow(() -> new ThirdPartyCustomerNotFoundException(email));

        Optional<PrivateThirdPartyDetail> privateThirdPartyDetail = privateThirdPartyDetailRepository.findByThirdPartyCustomer(thirdPartyCustomer);

        if(privateThirdPartyDetail.isPresent()) {
            ThirdPartyPrivateWrapper wrapper = new ThirdPartyPrivateWrapper();
            wrapper.setThirdPartyCustomer(thirdPartyCustomer);
            wrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail.get());
            return Optional.of(wrapper);
        }

        return Optional.empty();
    }

    @Override
    public ThirdPartyCompanyWrapper registerThirdPartyCompany(ThirdPartyCompanyWrapper thirdPartyCompanyWrapper) {
        // Check registration condition
        if(thirdPartyRepository.findByEmail(thirdPartyCompanyWrapper.getThirdPartyCustomer().getEmail()).isPresent()) {
            throw new AlreadyPresentEmailException(thirdPartyCompanyWrapper.getThirdPartyCustomer().getEmail());
        }

        // Register the customer
        ThirdPartyCustomer savedCustomer = thirdPartyRepository.saveAndFlush(thirdPartyCompanyWrapper.getThirdPartyCustomer());

        thirdPartyCompanyWrapper.getCompanyDetail().setThirdPartyCustomer(savedCustomer);
        CompanyDetail savedDetail = companyDetailRepository.saveAndFlush(thirdPartyCompanyWrapper.getCompanyDetail());

        // Construct return object
        ThirdPartyCompanyWrapper savedWrapper = new ThirdPartyCompanyWrapper();
        savedWrapper.setCompanyDetail(savedDetail);
        savedWrapper.setThirdPartyCustomer(savedCustomer);

        return savedWrapper;
    }

    @Override
    public ThirdPartyPrivateWrapper registerThirdPartyPrivate(ThirdPartyPrivateWrapper thirdPartyPrivateWrapper) {
        // Check registration condition
        if(thirdPartyRepository.findByEmail(thirdPartyPrivateWrapper.getThirdPartyCustomer().getEmail()).isPresent()) {
            throw new AlreadyPresentEmailException(thirdPartyPrivateWrapper.getThirdPartyCustomer().getEmail());
        }

        // Register the customer
        ThirdPartyCustomer savedCustomer = thirdPartyRepository.saveAndFlush(thirdPartyPrivateWrapper.getThirdPartyCustomer());

        thirdPartyPrivateWrapper.getPrivateThirdPartyDetail().setThirdPartyCustomer(savedCustomer);
        PrivateThirdPartyDetail privateThirdPartyDetail = privateThirdPartyDetailRepository.saveAndFlush(thirdPartyPrivateWrapper.getPrivateThirdPartyDetail());

        // Construct return object
        ThirdPartyPrivateWrapper savedWrapper = new ThirdPartyPrivateWrapper();
        savedWrapper.setThirdPartyCustomer(savedCustomer);
        savedWrapper.setPrivateThirdPartyDetail(privateThirdPartyDetail);

        return savedWrapper;
    }

    @Override
    public boolean verifyThirdPartyCredential(String email, String password) {
        // TODO
        return false;
    }
}
