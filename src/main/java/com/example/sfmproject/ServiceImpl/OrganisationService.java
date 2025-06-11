package com.example.sfmproject.ServiceImpl;

import com.example.sfmproject.Entities.Departement;
import com.example.sfmproject.Entities.Niveau;
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

    @Autowired
    DepartementService departementService;

    @Autowired
    NiveauService niveauService;



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

    @Override
    public List<Organisation> getAllDataForAdmin() {
        return organisationRepository.findAll();
    }

    @Override
    public List<Organisation> getHierarchy() {
        List<Organisation> organisations = getAllOrganisations();
        for (Organisation org : organisations) {
            List<Departement> departments = departementService.getDepartmentsByOrganisation(org.getIdOrganisation());
            for (Departement dept : departments) {
                List<Niveau> niveaux = niveauService.getNiveauxByDepartment(dept.getIdDepartement());
                dept.setNiveaux(niveaux);
            }
            org.setDepartments(departments);
        }
        return organisations;
    }

}
