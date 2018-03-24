# 单点登录系统

fork 自[https://github.com/a466350665/smart](https://github.com/a466350665/smart)

做了一点自己的改造，包括：

* 将smart-static里面的静态内容迁移到smart-sso-server，不需要再起两个容器
* 修改为，能登陆单点登录管理系统的用户，除了管理员，只能看到和维护自己被分配的应用的用户、角色、权限，这样做的主要目的是，不想把所有账号的维护都交给一个管理员，而是希望有个“其他系统管理员”的角色，这个角色可以登录单点登陆系统，管理自己身为管理员的那个系统的所有用户、权限和角色。
* 角色列表，选中了应用后，再新建角色，会把应用信息带到新建对话框，不需要再次选择
* ...

后续还需要优化，主要是考虑，客户端在验证token时并没有传递应用code，非法请求可能通过token绕过应用限制
其他想到再看