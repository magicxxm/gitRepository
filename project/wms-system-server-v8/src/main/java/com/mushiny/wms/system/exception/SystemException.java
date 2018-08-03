package com.mushiny.wms.system.exception;

public enum SystemException {

    /**
     * SYSTEM ENTITY EXCEPTION
     **/
    EX_SYS_CLIENT_NAME_UNIQUE,// 客户:{0}已存在!

    EX_SYS_CLIENT_NO_UNIQUE,// 客户:{0}已存在!

    EX_SYS_WAREHOUSE_NAME_UNIQUE,// 仓库:{0}已存在!

    EX_SYS_WAREHOUSE_NO_UNIQUE,// 仓库:{0}已存在!

    EX_SYS_MODULE_NAME_UNIQUE,// 模块:{0}已存在!

    EX_SYS_RESOURCE_KEY_LOCALE_UNIQUE,// 语言:{0}下的资源:{1}已存在!

    EX_SYS_ROLE_NAME_UNIQUE,// 角色:{0}已存在!

    EX_SYS_SELECTION_KEY_UNIQUE,// 数据字典:{0}已存在!

    EX_SYS_USER_NAME_UNIQUE,// 用户名:{0}已存在!

    EX_SYS_USER_PASSWORD_ERROR,//原密码输入错误！

    EX_SYS_USER_GROUP_NAME_UNIQUE,// 用户组:{0}已存在!

    EX_SYS_NOT_SYSTEM_CLIENT//请选择SYSTEM客户后进行操作！
}
