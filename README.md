# [JavaWeb-Session](https://github.com/happyflyer/JavaWeb-Session)

## 1. Servlet 进阶 API

在 Servlet 接口上，定义了与 Servlet 生命周期及请求服务相关的三个方法

- `init()`
- `service()`
- `destroy()`

```java
package javax.servlet;
import java.io.IOException;
public interface Servlet {
    public ServletConfig getServletConfig();
    public String getServletInfo();
    public void init(ServletConfig config) throws ServletException;
    public void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException;
    public void destroy();
}
```

每个 Servlet 都必须

- 由 web 容器读取 Servlet 设置信息（无论使用标注还是 web.xml）
- 初始化等

才可以真正成为一个 Servlet。

对于每个 Servlet 的设置信息，web 容器会为其生成

- 一个 `ServletConfig` 作为代表对象
- 你可以从该对象取得 Servlet 初始参数
- 以及代表整个 web 应用程序的 `ServletContext` 对象

```java
package javax.servlet;
import java.util.Enumeration;
public interface ServletConfig {
    public String getServletName();
    public ServletContext getServletContext();
    public String getInitParameter(String name);
    public Enumeration<String> getInitParameterNames();
}
```

在 Web 容器启动后，

- 会读取 Servlet 设置信息，将 Servlet 类加载并实例化
- 并为每个 Servlet 设置信息产生一个 `ServletConfig` 对象
- 而后调用 Servlet 接口的 `init()` 方法，并将产生的 `ServletConfig` 对象当作参数传入
- 这个过程只会在创建 Servlet 实例后发生一次
- 之后每次请求到来，调用 Servlet 实例的 `service()` 方法进行服务
- 每一次请求来到容器时，会产生 `HttpServletRequest` 与 `HttpServletResponse` 对象
- 并在调用 `service()` 方法时当作参数传入

```java
package javax.servlet;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
public abstract class GenericServlet
        implements Servlet, ServletConfig, java.io.Serializable {
    // ...
    private transient ServletConfig config;
    public GenericServlet() {}
    public ServletConfig getServletConfig() { ... }
    public String getServletInfo() { ... }
    public void init(ServletConfig config) throws ServletException { ... }
    public void init() throws ServletException {}
    public abstract void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException;
    public void destroy() {}
    public String getServletName() { ... }
    public ServletContext getServletContext() { ... }
    public String getInitParameter(String name) { ... }
    public Enumeration<String> getInitParameterNames() { ... }
    // ...
}
```

### 1.1. 使用 ServletConfig

- `ServletConfig` 相当于个别 Servlet 的设置信息代表对象
- 这意味着可以从 `ServletConfig` 中取得 Servlet 设置信息
- `ServletConfig` 定义了 `getInitParameter()` 和 `getInitParameterNames()` 方法
- 可以取得设置 Servlet 时的初始参数

#### 1.1.1. 实战 1

```xml
<servlet>
  <servlet-name>ConfigServlet</servlet-name>
  <servlet-class>org.example.java_web.session.servlet.ConfigServlet</servlet-class>
  <init-param>
    <param-name>param1</param-name>
    <param-value>value1</param-value>
  </init-param>
  <init-param>
    <param-name>param2</param-name>
    <param-value>value2</param-value>
  </init-param>
</servlet>
<servlet-mapping>
  <servlet-name>ConfigServlet</servlet-name>
  <url-pattern>/config.do</url-pattern>
</servlet-mapping>
```

```java
@WebServlet(name = "ConfigServlet", urlPatterns = "/config.do", initParams = {
        @WebInitParam(name = "param3", value = "value3"),
        @WebInitParam(name = "param4", value = "value4")
})
public class ConfigServlet extends HttpServlet {
}
```

#### 1.1.2. 实战 2

```html
<form action="login.do" method="post">
  <div class="form-group">
    <label for="username">用户名: </label>
    <input id="username" class="form-control" type="text" name="username" value="" />
  </div>
  <div class="form-group">
    <label for="password">密码: </label>
    <input id="password" class="form-control" type="password" name="password" value="" />
  </div>
  <button class="btn btn-primary" name="btn" value="submit">登录</button>
</form>
```

```java
@WebServlet(name = "LoginServlet", urlPatterns = "/login.do", initParams = {
        @WebInitParam(name = "success", value = "success.html"),
        @WebInitParam(name = "error", value = "error.html")
})
public class LoginServlet extends HttpServlet {
    private String successView;
    private String errorView;
    @Override
    public void init() {
        successView = getInitParameter("success");
        errorView = getInitParameter("error");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String trueUsername = "zhangsan";
        String truePassword = "123456";
        if (trueUsername.equals(username) && truePassword.equals(password)) {
            request.getRequestDispatcher(successView).forward(request, response);
        } else {
            request.getRequestDispatcher(errorView).forward(request, response);
        }
    }
}
```

### 1.2. 使用 ServletContext

- ServletContext 接口定义了运行 Servlet 的应用程序环境的一些行为与观点
- 可以使用 Servletcontext 实现对象来取得
  - 所请求资源的 URL
  - 设置与储存属性
  - 应用程序初始参数
  - 动态设置 Servlet 实例
- `getRequestDispatcher()`
  - 用来取得 `RequestDispatcher` 实例
  - 使用时路径的指定必须以 `/` 作为开头
  - 这个斜杠代表应用程序环境根目录（Context Root）
  - 以 `/` 作为开头有时称为环境相对（Context-relative）路径
  - 没有以 `/` 作为开头则称为请求相对（Request-relative）路径
  - 实际上 `HttpServletRequest` 的 `getRequestDispatcher()` 方法在实现时
  - 若是环境相对路径，则直接委托给 ServletContext 的 `getRequestDispatcher()`
  - 若是请求相对路径，则转换为环境相对路径
  - 再委托给 `ServletContext` 的 `getRequestDispatcher()` 来取得 `RequestDispatcher`
- `getResourcePaths()`
  - 如果想要知道 Web 应用程序的某个目录中有哪些文件
  - 则可以使用 `getResourcePaths()` 方法
  - 使用时指定路径必须以 `/` 作为开头，表示相对于应用程序环境根目录
  - 这个方法会连同 WEB-INF 的信息都列出来
  - 如果是个目录信息，则会以 `/` 作结尾
- `getResourceAsStream()`
  - 如果想在 Web 应用程序中读取某个文件的内容
  - 则可以使用 `getResourceAsStream()` 方法
  - 使用时指定路径必须以 `/` 作为开头，表示相对于应用程序环境根目录
  - 或者相对是 WEB-INF/lib 中 JAR 文件里 META-INF/resources 的路径
  - 运行结果会返回 `InputStream` 实例，接着就可以运用它来读取文件内容

#### 1.2.1. 实战

```java
@WebServlet("/context.do")
public class ContextServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // ...
        ServletContext context = getServletContext();
        // ...
        out.println("<p>context.getRealPath(\"/\"): "
                + context.getRealPath("/") + "</p>");
        out.println("<p>context.getResourcePaths(\"/\"): "
                + context.getResourcePaths("/") + "</p>");
        out.println("<p>context.getResourceAsStream(\"/\")："
                + context.getResourceAsStream("/") + "</p>");
        out.println("<p>new File(\"/filename\").getAbsolutePath(): "
                + new File("/filename").getAbsolutePath() + "</p>");
        out.println("<p>new File(\"filename\").getAbsolutePath(): "
                + new File("filename").getAbsolutePath() + "</p>");
       // ...
    }
}
```

```html
<p>context.getRealPath("/"): C:\Users\lifei\Applications\apache-tomcat-9.0.45\webapps\JavaWeb_Session_war\</p>
<p>context.getResourcePaths("/"): [/login.html, /error.html, /bootstrap-4.4.1-dist/, /filter/, /popper-2.5.3/, /META-INF/, /WEB-INF/, /jquery-3.5.1/, /index.html, /listener/, /avatars/, /success.html, /async.html]</p>
<p>context.getResourceAsStream("/")：null</p>
<p>new File("/filename").getAbsolutePath(): C:\filename</p>
<p>new File("filename").getAbsolutePath(): C:\Users\lifei\Applications\apache-tomcat-9.0.45\bin\filename</p>
```

## 2. 事件和监听器

### 2.1. ServletContext 事件、监听器

#### 2.1.1. ServletContextListener

- `ServletContextListener` 是“生命周期监听器”
- 如果想要知道何时 Web 应用程序已经初始化或即将结束销毁
- 可以实现 `servletContextListener` 接口

```java
package javax.servlet;
import java.util.EventListener;
public interface ServletContextListener extends EventListener {
    default public void contextInitialized(ServletContextEvent sce) {}
    default public void contextDestroyed(ServletContextEvent sce) {}
}
```

- `ServletContextListener` 可以直接使用 `@WebListener` 标注
- 而且必须实现 `ServletContextListener` 接口，这样容器就会在启动时加载并运行对应的方法
- 当 Web 容器调用 `contextInitialized()` 或 `contextDestroyed()` 时
- 会传入 `ServletContextEvent`，其封装了 `ServletContext`
- 可以通过 `servletContextEvent` 的 `getServletContext()` 方法取得 `ServletContext`
- 通过 `servletContext` 的 `getInitParameter()` 方法来读取初始参数
- 因此 Web 应用程序初始参数常被称为 `servletContext` 初始参数

在整个 Web 应用程序生命周期，

- Servlet 需共享的资料可以设置为 `ServletContext` 属性
- 可以通过 `servletContext` 的 `setAttribute()` 方法设置对象为 `ServletContext` 的属性
- 之后可通过 `servletContext` 的 `getAttribute()` 方法取出该属性
- 若要移除属性，则通过 `ServletContext` 的 `removeAttribute()` 方法
- 由于 `ServletContext` 在 Web 应用程序存活期间都会一直存在
- 所以设置为 `ServletContext` 属性的数据，除非主动移除，否则也是一直存活于 Web 应用程序中

#### 2.1.2. ServletContextAttributeListener

- `ServletContextAttributeListener` 是“监听属性改变的监听器”
- 如果想要对象被设置、移除或替换 `ServletContext` 属性
- 可以收到通知以进行一些操作，则可以实现 `ServletContextAttributeListener`

```java
package javax.servlet;
import java.util.EventListener;
public interface ServletContextAttributeListener extends EventListener {
    default public void attributeAdded(ServletContextAttributeEvent event) {}
    default public void attributeRemoved(ServletContextAttributeEvent event) {}
    default public void attributeReplaced(ServletContextAttributeEvent event) {}
}
```

#### 2.1.3. 实战

```xml
<context-param>
  <param-name>AVATAR_DIR</param-name>
  <param-value>/avatars</param-value>
</context-param>
```

```java
@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String avatarDir = context.getInitParameter("AVATAR_DIR");
        context.setAttribute("avatarDir", avatarDir);
        System.out.println("Context Initialized");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context Destroyed");
    }
}
```

```java
@WebListener
public class MyServletContextAttributeListener
        implements ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Added: "
                + event.getName() + " : " + event.getValue());
    }
    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Removed: "
                + event.getName() + " : " + event.getValue());
    }
    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        System.out.println("Context Attribute Replaced: "
                + event.getName() + " : " + event.getValue());
    }
}
```

```java
@WebServlet("/avatar.view")
public class AvatarServlet extends HttpServlet {
    private String avatarDir;
    @Override
    public void init() {
        avatarDir = (String) getServletContext().getAttribute("avatarDir");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // ...
        for (String avatar : getServletContext().getResourcePaths(avatarDir)) {
            out.print("<img src='" + request.getContextPath() + avatar + "'>");
        }
        // ...
    }
}
```

```bash
Context Attribute Added: avatarDir : /avatars
Context Initialized
```

```html
<img src="/JavaWeb_Session_war/avatars/pic.png" />
```

### 2.2. HttpSession 事件、监听器

与 `HttpSession` 相关的监听器有四个

- `HttpSessionListener`
- `HtpSessionAttributeListener`
- `HttpSessionBindingListener`
- `HtpSessionActivationListener`

```java
package javax.servlet.http;
import java.util.EventListener;
public interface HttpSessionListener extends EventListener {
    default public void sessionCreated(HttpSessionEvent se) {}
    default public void sessionDestroyed(HttpSessionEvent se) {}
}
```

```java
package javax.servlet.http;
import java.util.EventListener;
public interface HttpSessionAttributeListener extends EventListener {
    default public void attributeAdded(HttpSessionBindingEvent event) {}
    default public void attributeRemoved(HttpSessionBindingEvent event) {}
    default public void attributeReplaced(HttpSessionBindingEvent event) {}
}
```

```java
package javax.servlet.http;
import java.util.EventListener;
public interface HttpSessionBindingListener extends EventListener {
    default public void valueBound(HttpSessionBindingEvent event) {}
    default public void valueUnbound(HttpSessionBindingEvent event) {}
}
```

- `HttpSessionActivationListener` 是“对象迁移监听器”
- 其定义了两个方法 `sessionWillPassivate()` 与 `sessionDidActivate()`
- 很多情况下，几乎不会使用到 `HttpSessionActivationListener`
- 在使用到分布式环境时，应用程序的对象可能分散在多个 JVM 中
- 当 `HttpSession` 要从一个 JVM 迁移至另一个 JVM 时
- 必须先在原本的 JVM 上序列化（Serialize）所有的属性对象
- 在这之前若属性对象有实现 `HttpSessionActivationListener`
- 就会调用`sessionWillPassivate()` 方法
- 而 `HttpSession` 迁移至另一个 JVM 后
- 就会对所有属性对象作反序列化，此时会调用 `sessionDidActivate()` 方法

```java
package javax.servlet.http;
import java.util.EventListener;
public interface HttpSessionActivationListener extends EventListener {
    default public void sessionWillPassivate(HttpSessionEvent se) {}
    default public void sessionDidActivate(HttpSessionEvent se) {}
}
```

#### 2.2.1. 实战

```html
<form action="login.do" method="post">
  <div class="form-group">
    <label for="username">用户名: </label>
    <input id="username" class="form-control" type="text" name="username" value="" />
  </div>
  <div class="form-group">
    <label for="password">密码: </label>
    <input id="password" class="form-control" type="password" name="password" value="" />
  </div>
  <button class="btn btn-primary" name="btn" value="submit">登录</button>
</form>
```

```java
@WebServlet("/listener/login.do")
public class LoginServlet extends HttpServlet {
    private final Map<String, String> users;
    public LoginServlet() {
        users = new HashMap<>();
        users.put("zhangsan", "123456");
        users.put("lisi", "000000");
        users.put("wangwu", "888888");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (users.containsKey(username) && users.get(username).equals(password)) {
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("user", new User(username));
            String welcomeView = "/listener/welcome.view";
            getServletContext().getRequestDispatcher(welcomeView).forward(request, response);
        } else {
            String loginView = "login.html";
            response.sendRedirect(loginView);
        }
    }
}
```

```java
@WebServlet("/listener/welcome.view")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>WelcomeServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>目前在线人数: " + MyHttpSessionListener.getCounter() + "人 </p>");
        HttpSession session = request.getSession();
        if (session != null) {
            String username = (String) session.getAttribute("username");
            out.println("<p>欢迎, " + username + "</p>");
            out.println("<a href='logout.do'>注销</a>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
```

```java
@WebServlet("/listener/logout.do")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("user");
        String loginView = "login.html";
        response.sendRedirect(loginView);
    }
}
```

```java
public class User implements HttpSessionBindingListener {
    private String username;
    private String password;
    public User(String name) {
        this.username = name;
    }
    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        System.out.println(this + " 已经绑定");
    }
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        System.out.println(this + " 已经解除");
    }
    // ...
}
```

```java
@WebListener
public class MyHttpSessionListener implements HttpSessionListener {
    private static int counter = 0;
    public static int getCounter() {
        return counter;
    }
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        MyHttpSessionListener.counter++;
        HttpSession session = se.getSession();
        String user = (String) session.getAttribute("user");
        // 修改数据库中字段为登录状态，如果已经是登录状态，则不允许登录
        System.out.println("Session Created: " + se.getSession());
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        MyHttpSessionListener.counter--;
        HttpSession session = se.getSession();
        String user = (String) session.getAttribute("user");
        // 修改数据库字段为注销状态
        System.out.println("Session Destroyed: " + se.getSession());
    }
}
```

```java
@WebListener
public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Added: "
                + event.getName() + " : " + event.getValue());
    }
    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Removed: "
                + event.getName() + " : " + event.getValue());
    }
    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("Session Attribute Replaced: "
                + event.getName() + " : " + event.getValue());
    }
}
```

```bash
Session Created: org.apache.catalina.session.StandardSessionFacade@7cdec7f
Session Attribute Added: username : zhangsan
User{username='zhangsan'} 已经绑定
Session Attribute Added: user : User{username='zhangsan'}
```

```bash
Session Attribute Removed: username : zhangsan
User{username='zhangsan'} 已经解除
Session Attribute Removed: user : User{username='zhangsan'}
```

### 2.3. HttpServletRequest 事件、监听器

```java
package javax.servlet;
import java.util.EventListener;
public interface ServletRequestListener extends EventListener {
    default public void requestDestroyed(ServletRequestEvent sre) {}
    default public void requestInitialized(ServletRequestEvent sre) {}
}
```

```java
package javax.servlet;
import java.util.EventListener;
public interface ServletRequestAttributeListener extends EventListener {
    default public void attributeAdded(ServletRequestAttributeEvent srae) {}
    default public void attributeRemoved(ServletRequestAttributeEvent srae) {}
    default public void attributeReplaced(ServletRequestAttributeEvent srae) {}
}
```

## 3. 过滤器

- 性能评测
- 用户验证
- 字符替换
- 编码设置

```java
package javax.servlet;
import java.io.IOException;
public interface Filter {
    default public void init(FilterConfig filterConfig) throws ServletException {}
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException;
    default public void destroy() {}
}
```

```java
package javax.servlet;
import java.util.Enumeration;
public interface FilterConfig {
    public String getFilterName();
    public ServletContext getServletContext();
    public String getInitParameter(String name);
    public Enumeration<String> getInitParameterNames();
}
```

```java
package javax.servlet;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
public abstract class GenericFilter
        implements Filter, FilterConfig, java.io.Serializable {
    // ...
    private transient FilterConfig config;
    public GenericFilter() { }
    public FilterConfig getFilterConfig() { ... }
    public void init(FilterConfig config) throws ServletException { ... }
    public void init() throws ServletException { ... }
    public String getFilterName() { ... }
    public ServletContext getServletContext() { ... }
    public String getInitParameter(String name) { ... }
    public Enumeration<String> getInitParameterNames() { ... }
}
```

- Filter 接口的 `doFilter()` 方法则类似于 Servlet 接口的 `service()` 方法
- 当请求来到容器，而容器发现调用 Servlet 的 `service()` 方法前
- 可以应用某过滤器时，就会调用该过滤器的 `doFilter()` 方法
- 可以在 `doFilter()` 方法中进行 `service()` 方法的前置处理
- 而后决定是否调用 `FilterChain` 的 `doFilter()` 方法
- 如果调用了 `FilterChain` 的 `doFilter()` 方法，就会运行下一个过滤器
- 如果没有下个过滤器了，就调用请求目标 Servlet 的 `service()` 方法
- 如果因为某个情况（如用户没有通过验证）而没有调用 `FilterChain` 的 `doFilter()`
- 则请求就不会继续交给接下来的过滤器或目标 Servlet
- 这时就是所谓的拦截请求（从 Servlet 的观点来看，根本不知道浏览器有发出请求）
- `FilterChain` 的 `doFilter()` 的实现原理，概念上类似以下

```java
Filter filter = filterIterator.next()
if (filter != null) {
    filter.doFilter(request, response, this);
} else {
    targetServlet.service(request, response);
}
```

- 在陆续调用完 Filter 实例的 `doFilter()` 仍至 Servlet 的 `service()` 之后
- 流程会以堆栈顺序返回
- 所以在 `FilterChain` 的 `doFilter()` 运行完毕后
- 就可以针对 `service()` 方法做后续处理
- 只需要知道 `FilterChain` 运行后会以堆栈顺序返回即可
- 在实现 `Filter` 接口时，不用理会这个 `Filter` 前后是否有其他 `Filter`
- 应该将之作为个独立的元件设计
- 如果在调用 `Filter` 的 `doFilter()` 期间，因故抛出 `unavailableException`
- 此时不会继续下一个 `Filter`
- 容器可以检验异常的 `isPermanent()`，如果不是 true，则可以在稍后重试 `Filter`

```java
// service 前置处理
chain.doFilter(request, response);
// service 后置处理
```

### 3.1. 初始参数

```java
@WebFilter(filterName = "ConfigFilter", urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "param1", value = "value1"),
                @WebInitParam(name = "param2", value = "value2")
        })
public class ConfigFilter extends HttpFilter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        String param1 = config.getInitParameter("param1");
        String param2 = config.getInitParameter("param2");
        System.out.println("ConfigFilter init : param1 : " + param1);
        System.out.println("ConfigFilter init : param2 : " + param2);
    }
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain
    ) throws ServletException, IOException {
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
        System.out.println("ConfigFilter destroy");
    }
}
```

```bash
ConfigFilter init : param1 : value1
ConfigFilter init : param2 : value2
```

### 3.2. 性能评测

```java
@WebFilter(filterName = "PerformanceFilter", urlPatterns = {"/*"},
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.ERROR,
                DispatcherType.ASYNC
        })
public class PerformanceFilter extends HttpFilter {
    @Override
    public void init(FilterConfig config) throws ServletException {
        System.out.println("PerformanceFilter init");
    }
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain
    ) throws ServletException, IOException {
        long begin = System.currentTimeMillis();
        chain.doFilter(request, response);
        long end = System.currentTimeMillis();
        System.out.printf("Request %s process in %d milliseconds%n",
                request.getServletPath(), end - begin);
    }
    @Override
    public void destroy() {
        System.out.println("PerformanceFilter destroy");
    }
}
```

```bash
Request /listener/welcome.view process in 1 milliseconds
Request /listener/login.do process in 18 milliseconds
```

### 3.3. 请求封装器

```java
package javax.servlet;
import java.io.*;
import java.util.*;
public interface ServletRequest {
}
```

```java
package javax.servlet;
import java.io.*;
import java.util.*;
public class ServletRequestWrapper implements ServletRequest {
}
```

```java
package javax.servlet.http;
import java.io.*;
import java.util.*;
import javax.servlet.*;
public interface HttpServletRequest extends ServletRequest {
}
```

```java
package javax.servlet.http;
import java.io.*;
import java.util.*;
import javax.servlet.*;
public class HttpServletRequestWrapper extends ServletRequestWrapper
    implements HttpServletRequest {
}
```

### 3.4. 字符替换

```java
public class EscapeWrapper extends HttpServletRequestWrapper {
    public EscapeWrapper(HttpServletRequest request) {
        super(request);
    }
    @Override
    public String getParameter(String name) {
        String value = getRequest().getParameter(name);
        return StringEscapeUtils.escapeHtml(value);
    }
}
```

```java
@WebFilter(filterName = "EscapeFilter", urlPatterns = "/*")
public class EscapeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("EscapeFilter init");
    }
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest requestWrapper = new EscapeWrapper((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
    @Override
    public void destroy() {
        System.out.println("EscapeFilter destroy");
    }
}
```

```html
<form action="form.do" method="post">
  <div class="form-group">
    <label for="content">内容: </label>
    <input id="content" class="form-control" type="text" name="content" value="" />
  </div>
  <button class="btn btn-primary" name="btn" value="submit">提交</button>
</form>
```

```java
@WebServlet("/filter/form.do")
public class FormServlet extends HttpServlet {
    // ...
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // ...
        // <a href="http://www.baidu.com">百度一下，你就知道</a>
        String content = request.getParameter("content");
        // ...
        out.println("<p>" + content + " </p>");
        // ...
    }
}
```

```html
<p>&lt;a href=&quot;http://www.baidu.com&quot;&gt;&#30334;&#24230;&#19968;&#19979;&#65292;&#20320;&#23601;&#30693;&#36947;&lt;/a&gt;</p>
```

### 3.5. 编码设置

```java
public class EncodingWrapper extends HttpServletRequestWrapper {
    private final String encoding;
    public EncodingWrapper(HttpServletRequest request, String encoding) {
        super(request);
        this.encoding = encoding;
    }
    @Override
    public String getParameter(String name) {
        String value = getRequest().getParameter(name);
        if (value != null) {
            try {
                byte[] bytes = value.getBytes(StandardCharsets.ISO_8859_1);
                value = new String(bytes, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }
}
```

```java
@WebFilter(filterName = "EncodingFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8")
        })
public class EncodingFilter implements Filter {
    private String encoding;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        System.out.println("EncodingFilter init");
    }
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String getMethod = "GET";
        if (getMethod.equals(req.getMethod())) {
            req = new EncodingWrapper(req, encoding);
        } else {
            req.setCharacterEncoding(encoding);
        }
        chain.doFilter(req, response);
    }
    @Override
    public void destroy() {
        System.out.println("EncodingFilter destroy");
    }
}
```

```java
@WebServlet("/filter/form.do")
public class FormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // ...
        String content = request.getParameter("content");
        // ...
        out.println("<p>" + content + " </p>");
        // ...
    }
    // ...
}
```

### 3.6. 响应封装器

```java
package javax.servlet;
import java.io.*;
import java.util.*;
public interface ServletResponse {
}
```

```java
package javax.servlet;
import java.io.*;
import java.util.*;
public class ServletResponseWrapper implements ServletResponse {
}
```

```java
package javax.servlet.http;
import java.io.*;
import java.util.*;
import javax.servlet.*;
public interface HttpServletResponse extends ServletResponse {
}
```

```java
package javax.servlet.http;
import java.io.*;
import java.util.*;
import javax.servlet.*;
public class HttpServletResponseWrapper extends ServletResponseWrapper
    implements HttpServletResponse {
}
```

### 3.7. 响应压缩

```java
public class GzipServletOutputStream extends ServletOutputStream {
    private final GZIPOutputStream gzipOutputStream;
    public GzipServletOutputStream(ServletOutputStream servletOutputStream)
            throws IOException {
        this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
    }
    @Override
    public boolean isReady() {
        return false;
    }
    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
    @Override
    public void write(int b) throws IOException {
        gzipOutputStream.write(b);
    }
    public GZIPOutputStream getGzipOutputStream() {
        return gzipOutputStream;
    }
}
```

```java
public class CompressionWrapper extends HttpServletResponseWrapper {
    private GzipServletOutputStream gzServletOutputStream;
    private PrintWriter printwriter;
    public CompressionWrapper(HttpServletResponse response) {
        super(response);
    }
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printwriter != null) {
            throw new IllegalStateException();
        }
        if (gzServletOutputStream == null) {
            gzServletOutputStream = new GzipServletOutputStream(
                    getResponse().getOutputStream()
            );
        }
        return gzServletOutputStream;
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        if (gzServletOutputStream != null) {
            throw new IllegalStateException();
        }
        if (printwriter == null) {
            gzServletOutputStream = new GzipServletOutputStream(
                    getResponse().getOutputStream()
            );
            OutputStreamWriter osw = new OutputStreamWriter(
                    gzServletOutputStream,
                    getResponse().getCharacterEncoding()
            );
            printwriter = new PrintWriter(osw);
        }
        return printwriter;
    }
    public GZIPOutputStream getGzipOutputStream() {
        if (this.gzServletOutputStream == null) {
            return null;
        }
        return this.gzServletOutputStream.getGzipOutputStream();
    }
}
```

```java
@WebFilter(filterName = "CompressionFilter", urlPatterns = "/*")
public class CompressionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CompressionFilter init");
    }
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain
    ) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String encodings = req.getHeader("accept-encoding");
        if (encodings != null && (encodings.contains("gzip"))) {
            CompressionWrapper responseWrapper = new CompressionWrapper(resp);
            responseWrapper.setHeader("content-encoding", "gzip");
            chain.doFilter(request, responseWrapper);
            GZIPOutputStream gzipOutputStream = responseWrapper.getGzipOutputStream();
            if (gzipOutputStream != null) {
                gzipOutputStream.finish();
            } else {
                chain.doFilter(request, response);
            }
        }
    }
    @Override
    public void destroy() {
        System.out.println("CompressionFilter destroy");
    }
}
```

## 4. 异步处理

### 4.1. AsyncContext 简介

```java
package javax.servlet;
import java.io.*;
import java.util.*;
public interface ServletRequest {
    public AsyncContext startAsync() throws IllegalStateException;
    public AsyncContext startAsync(
            ServletRequest servletRequest,
            ServletResponse servletResponse
    ) throws IllegalStateException;
}
```

- 这两个方法都会返回 `AsyncContext` 接口的实现对象
- 前者会直接利用原有的请求与响应对象来创建 `AsyncContext`
- 后者可以传入自行创建的请求、响应封装对象
- 在调用 `startAsync()` 方法取得 `AsyncContext` 对象之后
- 此次请求的响应会被延后，并释放容器分配的线程

`AsyncContext` 使用

- 可以通过 `AsyncContext` 的 `getRequest()`、`getResponse()` 方法取得请求、响应对象
- 此次对客户端的响应将暂缓至调用 `AsyncContext` 的 `complete()` 或 `dispatch()` 方法为止
- 前者表示响应完成
- 后者表示将调派指定的 URL 进行响应

```java
@WebServlet(name = "AsyncServlet", urlPatterns = "/async.do", asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        // 开始异步处理，释放请求线程
        AsyncContext ctx = request.startAsync();
        // 创建 AsyncRequest，调度线程
        executorService.submit(new AsyncRequest(ctx));
    }
    @Override
    public void destroy() {
        // 关闭线程池
        executorService.shutdown();
    }
}
```

```java
public class AsyncRequest implements Runnable {
    private final AsyncContext ctx;
    public AsyncRequest(AsyncContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public void run() {
        try {
            // 模拟冗长请求
            Thread.sleep(10000);
            PrintWriter out = ctx.getResponse().getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>AsyncServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>久等了 ...</p>");
            out.println("</body>");
            out.println("</html>");
            out.close();
            // 对客户端完成响应
            ctx.complete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

### 4.2. 模拟服务器推播

HTTP 是基于请求、响应模型

- HTTP 服务器无法直接对客户端（浏览器）传送信息，因为没有请求就不会有响应
- 在这种请求、响应模型下，如果客户端想要获得服务器端应用程序的最新状态
- 必须以定期（或不定期）方式发送请求，查询服务器端的最新状态

持续发送请求以查询服务器端最新状态，

- 这种方式的问题在于耗用网络流量
- 如果多次请求过程后，服务器端应用程序状态并没有变化
- 那这多次的请求耗用的流量就是浪费的

一个解决的方式是，

- 服务器端将每次请求的响应延后，直到服务器端应用程序状态有变化时再进行响应
- 当然这样的话，客户端将会处于等待响应的状态
- 如果是浏览器，可以搭配 Ajax 异步请求技术，而用户将不会因此而被迫停止网页的操作
- 然而服务器端延后请求的话，若是 Servlet/JSP 技术，等于该请求占用一个线程
- 若客户端很多，每个请求都占用线程，将会使得服务器端的性能负担很重

Servlet 3.0 中提供的异步处理技术，

- 可以解决每个请求占用线程的问题
- 若搭配浏览器端 Ajax 异步请求技术，就可达到类似服务器端主动通知浏览器的行为
- 也就是所谓的服务器端推播（Server push）

```html
<!DOCTYPE html>
<html lang="zh">
  <head>
    <meta charset="UTF-8" />
    <title>async</title>
    <script>
      function asyncUpdate() {
        var xhr;
        if (window.XMLHttpRequest) {
          xhr = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
          xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }
        xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
            if (xhr.status === 200) {
              document.getElementById('data').innerHTML = xhr.responseText;
              asyncUpdate();
            }
          }
        };
        xhr.open('GET', 'asyncNum.do?timestamp=' + new Date().getTime());
        xhr.send();
      }
      window.onload = asyncUpdate;
    </script>
  </head>
  <body>
    <div class="container"><label>实时资料：</label> <span id="data"></span></div>
  </body>
</html>
```

```java
@WebServlet(name = "AsyncNumServlet", urlPatterns = {"/asyncNum.do" },
        asyncSupported = true)
public class AsyncNumServlet extends HttpServlet {
    private List<AsyncContext> asyncList;
    @Override
    public void init() {
        asyncList = (List<AsyncContext>) getServletContext().getAttribute("async");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AsyncContext ctx = request.startAsync();
        synchronized (asyncList) {
            asyncList.add(ctx);
        }
    }
}
```

```java
@WebListener
public class WebInitListener implements ServletContextListener {
    /**
     * 所有异步请求的 AsyncContext 将存储在这个 List
     */
    private List<AsyncContext> async = new ArrayList<>();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("async", async);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // 模拟不定时产生随机数字
                        Thread.sleep((int) (Math.random() * 3000));
                        double num = Math.random() * 10;
                        synchronized (async) {
                            // 逐一完成异步请求
                            for (AsyncContext ctx : async) {
                                ctx.getResponse().getWriter().println(num);
                                ctx.complete();
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        System.out.println("Async Context Initialized");
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Async Context Destroyed");
    }
}
```

### 4.3. 更多细节

- 如果 Servlet 或过滤器的 `asyncSupported` 被标示为 `true`
- 则它们支持异步请求处理
- 在不支持异步处理的 Servlet 或过滤器中调用 `startAsync()`
- 会抛出 `IllegalStateException`
- 当在支持异步处理的 Servlet 或过滤器中调用请求对象的 `startAsync()` 方法时
- 该次请求会离开容器所分配的线程，这意味着必须响应处理流程会返回
- 也就是若有过滤器，也会依序返回（也就是各自完成 `filterChain` 的 `doFilter()` 方法）
- 但最终的响应被延迟

可以

- 调用 `AsyncContext` 的 `complete()` 方法完成响应
- 而后调用 `forward()` 方法
- 将响应转发给别的 Servlet/JSP 处理 `AsyncContext` 的 `forward()`

不可以

- 自行在同一个 `AsyncContext` 上同时调用 `complete()` 与 `forward()`
- 否则会抛出 `IllegalStateException`

不可以

- 在两个异步处理的 Servlet 间派送前，连续调用两次 `startAsync()`
- 否则会抛出 `IllegalStateException`

将请求从支持异步处理的 Servlet（`asyncSupported` 被标示为 `true`）

- 派送至一个同步处理的 Servlet 是可行的（`asyncSupported` 被标示为 `false`）
- 此时，容器会负责调用 `AsyncContext` 的 `complete()`

如果从一个同步处理的 Servlet

- 派送至一个支持异步处理的 Servlet
- 在异步处理的 Servlet 中调用 `AsyncContext` 的 `startAsync()`
- 将会抛出 `illegalStateException`
- 如果对 `AsyncContext` 的起始、完成、超时或错误发生等事件有兴趣
- 可以实现 `AsyncListener`

```java
package javax.servlet;
import java.io.IOException;
import java.util.EventListener;
public interface AsyncListener extends EventListener {
    public void onComplete(AsyncEvent event) throws IOException;
    public void onTimeout(AsyncEvent event) throws IOException;
    public void onError(AsyncEvent event) throws IOException;
    public void onStartAsync(AsyncEvent event) throws IOException;
}
```

- `AsyncContext` 有个 `addListener()` 方法
- 可以加入 `AsyncListener` 的实现对象
- 在对应事件发生时会调用 `AsyncListener` 实现对象的对应方法
- 如果调用 `AsyncContext` 的 `dispatch()`
- 将请求调派给别的 Servlet
- 则可以通过请求对象的 `getAttribute()` 取得以下属性
  - `javax.servlet.async.request_uri` = `HttpServletRequest.getRequestURI()`
  - `javax.servlet.async.context_path` = `HttpServletRequest.getContextPath()`
  - `javax.servlet.async.servlet_path` = `HttpServletRequest.getServletPath()`
  - `javax.servlet.async.path_info` = `HttpServletRequest.getPathInfo()`
  - `javax.servlet.async.query_string` = `HttpServletRequest.getQueryString()`
