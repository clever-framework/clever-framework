## JWT 的组成结构

为了防止数据篡改，我们不可能明文发送像上面那样的 json，而是进行了签名之后，以字符串的形式发送给前端，大概像这样：

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJudWxsaiJ9.W8hfFfcMVgmlAhTRUl4GHNAq4tq_MWJGB1bv-r9wMCE
```

它是一个很长的字符串，中间用 . 分割成三段，这三段分别代表：

- Header：头部，记录了一些元数据，例如采用何种算法，令牌类型等。
- Payload：负载，存储我们的关键信息。
- Signature：签名，前两部分的签名，防止数据篡改。

我们主要关注 Payload，JWT 官方规定了 7 个供选择的字段：

- iss (issuer)：签发人
- exp (expiration time)：过期时间
- sub (subject)：主题
- aud (audience)：受众
- nbf (Not Before)：生效时间
- iat (Issued At)：签发时间
- jti (JWT ID)：编号