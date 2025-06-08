package com.example.sfmproject.Services;


import com.example.sfmproject.Entities.Organisation;

import java.util.List;

public interface IOrganisationService {
    List<Organisation> getAllOrganisations();
    Organisation getOrganisationById(Long id);
    void saveOrganisation(Organisation organisation);
    void deleteOrganisation(Long id);
    void updateOrganisation(Long id);

    List<Organisation> getAllDataForAdmin();

}
