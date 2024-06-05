package org.pi.server.mapper;

import org.junit.jupiter.api.Test;
import org.pi.server.model.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrudTest {
    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void insert() {
//        Role role = new Role();
//        role.setRoleName(RoleEnum.ADMIN);
//        roleMapper.insert(role);
//        System.out.println(role.getId());
    }

    @Test
    public void select() {
        Role role = roleMapper.selectById(1);
        System.out.println(role);
    }

    @Test
    public void selectAll() {
        roleMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void update() {
//        Role role = roleMapper.selectById(1);
//        role.setRoleName(RoleEnum.USER);
//        roleMapper.updateById(role);
    }

    @Test
    public void delete() {
        // roleMapper.deleteById(1);
    }

}
