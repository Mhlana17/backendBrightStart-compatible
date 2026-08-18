package za.ac.cput.service;

import za.ac.cput.domain.Admin;

import java.util.List;

public interface IAdminService extends IService<Admin, Long>{
    List<Admin> getAll();
}
