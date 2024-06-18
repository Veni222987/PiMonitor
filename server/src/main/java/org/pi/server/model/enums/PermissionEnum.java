package org.pi.server.model.enums;

import lombok.Getter;

/**
 * @author hu1hu
 */

@Getter
public enum PermissionEnum {
    ACTION_ALL("action_all"),
    RANGE_ALL_USER("range_all_user"),
    RANGE_ALL_HOST("range_all_host"),
    CREATE_USER("create_user"),
    DELETE_USER("delete_user"),
    UPDATE_USER_INFORMATION("update_user_information"),
    VIEW_USER_LIST("view_user_list"),
    UPDATE_ROLE_TO_USER("update_role_to_user"),
    VIEW_USER_ROLE("view_user_role"),
    ADD_HOST("add_host"),
    DELETE_HOST("delete_host"),
    UPDATE_HOST_INFORMATION("update_host_information"),
    VIEW_HOST_LIST("view_host_list"),
    ASSIGN_ROLE_TO_USER("assign_role_to_user"),
    REMOVE_HOST_FROM_USER("remove_host_from_user"),
    VIEW_USER_ACCESS_HOST("view_user_access_host"),
    NONE("none");
    private final String permissionName;

    PermissionEnum(String permissionName) {
        this.permissionName = permissionName;
    }

}
