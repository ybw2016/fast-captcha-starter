### ------ fast-captcha-starter - 发送短信基础包 ------

### 一。主要功能：发送短信功能;
- 核心功能：集成发生短信、短信次数校验、分布式锁等功能，使得下游调用非常简单;
- 发送方式：静默发送/非静默发送;

### 二。实现原理：
<h5> 1. 将发送短信、验证短信、一段时间内发送短信频次控制、切换图片切换码、生成图片验证码等公共模块封装成发送短信流程，
        保留用户自定义扩展的钩子，从而实现将发送短信与用户业务逻辑分离;

### 三。主要类说明（扩展点）
<h5> 1. SmsAspect：将发送短信的场景和发送短信流程衔接起来，可以理解为：桥梁;
<h5> 2. AbstractSmsCaptchaService：发送短信、验证短信的核心类;
<h5> 3. SmsCacheService：缓存业务数据、短信次数等数据;
<h5> 4. SmsConfig：发送短信相关的业务类配置;
<h5> 5. UniqueKeyGettable：使用方手动获取唯一业务key;
<h5> 6. Msg：发送短信保存到reids的上下文对象
<h5> 7. SmsCacheService：缓存管理类
<h5> 8. CaptchaAutoConfig：自动配置类
<h5> 9. SendSmsCode/CheckSmsCode: 主要注解
<h5> 10. META-INF/spring.factories: springboot加载starter


### 四。demo样例
<h5> 1. 主要入口类：AccountController
<h5> 2. base包：全局基础类;
<h5> 2. RedisTool：只提供样例暂未包含其实现;
