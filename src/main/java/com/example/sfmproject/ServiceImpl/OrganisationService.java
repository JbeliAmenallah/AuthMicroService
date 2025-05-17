package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Repositories.organisationRepository;

import com.example.sfmproject.Entities.Organisation;
import com.example.sfmproject.Services.IOrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrganisationService implements IOrganisationService {
    @Autowired
    organisationRepository organisationRepository;

    @Override
    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }

    @Override
    public Organisation getOrganisationById(Long id) {
        return organisationRepository.findById(id).get();
    }

    @Override
    public void saveOrganisation(Organisation organisation) {
    organisationRepository.save(organisation);
    }

    @Override
    public void deleteOrganisation(Long id) {
    organisationRepository.deleteById(id);
    }

    @Override
    public void updateOrganisation(Long id) {
    organisationRepository.save(organisationRepository.findById(id).get());
    }


}
