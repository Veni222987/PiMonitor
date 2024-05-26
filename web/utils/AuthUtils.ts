import { del, get, Itf, post, put } from "./APIUtils";
import JWT from "jsonwebtoken";
import { PromiseUtils } from "./PromiseUtils";
import { getLocalStorage, removeLocalStorage, setLocalStorage } from "./StorageUtils";

// 时间是以秒为单位
export interface JwtPayload {
  [key: string]: any
  iss?: string | undefined
  sub?: string | undefined
  aud?: string | string[] | undefined
  exp?: number | undefined
  nbf?: number | undefined
  iat?: number | undefined
  jti?: string | undefined
}

export interface Payload extends JwtPayload {
  // TODO: JWT内容
}

const TokenKey = "token";
export type TokenType = typeof DefaultTokenType
export const DefaultTokenType = "login";
export const AuthorizationKey = "Authorization";

export function authGet<I = any, O = any>(
  url: string, type: TokenType = DefaultTokenType): Itf<I, O> {
  return (d, h) => get<I, O>(url)(d, {
    [AuthorizationKey]: useToken(type), ...h
  });
}
export function authPost<I = any, O = any>(
  url: string, type: TokenType = DefaultTokenType): Itf<I, O> {
  return (d, h) => post<I, O>(url)(d, {
    [AuthorizationKey]: useToken(type), ...h
  });
}
export function authPut<I = any, O = any>(
  url: string, type: TokenType = DefaultTokenType): Itf<I, O> {
  return (d, h) => put<I, O>(url)(d, {
    [AuthorizationKey]: useToken(type), ...h
  });
}
export function authDel<I = any, O = any>(
  url: string, type: TokenType = DefaultTokenType): Itf<I, O> {
  return (d, h) => del<I, O>(url)(d, {
    [AuthorizationKey]: useToken(type), ...h
  });
}

class Token {
  // // TOKEN有效总时间
  // public static Duration = 0;
  // // TOKEN更新时间（测试30秒）
  // public static UpdateDuration = 0;

  public value: string | undefined;
  public data: JwtPayload | string | null | undefined;

  /**
   * 是否有效
   */
  public isValid() {

    // @ts-ignore
    return !this.isOutOfDate() && this.data.count == null || this.data.count == -1 || this.data.count > 0;
  }

  // 是否过时
  public isOutOfDate() {
    const now = Date.now();
    // @ts-ignore
    return now >= this.data.exp * 1000;
  }

  public static create(token: string | Token) {
    if (token instanceof Token) return token;
    const res = new Token();
    res.value = token;
    res.data = JWT.decode(token);
    return res;
  }

  public static invalid() {
    let res = new Token();
    res.value = "";
    return res;
  }
}

const JWTKey = "1234567"
const JWTExpireTime = 60 * 60 * 24 * 3;

const _tokens: { [K in TokenType]?: Token } = {}; // Token.invalid();

// region Token管理

/**
 * 读取保存的有效JWT
 */
export function loadToken(type: TokenType = DefaultTokenType) {
  const token = getTokenFromCache(type)
  token && setupToken(token)
  return _tokens[type];
}

/**
 * 获取Token值
 */
export function getToken(type: TokenType = DefaultTokenType) {
  const res = _tokens[type] || loadToken(type);
  if (!res?.isValid()) removeToken(type)
  return res
}

/**
 * 从缓存中获取Token值
 */
function getTokenFromCache(type: TokenType = DefaultTokenType) {
  const tokenStr = getLocalStorage(`${TokenKey}-${type}`);
  return tokenStr && Token.create(tokenStr);
}

/**
 * 获取Token值
 */
export function getTokenValue(type: TokenType = DefaultTokenType) {
  return getToken(type)?.value
}

/**
 * 获取Data
 */
export function getPayload(type: TokenType = DefaultTokenType) {
  return getToken(type)?.data
}

/**
 * 获取Token值
 */
export function useToken(type: TokenType = DefaultTokenType) {
  const token = getToken(type);
  // @ts-ignore
  if (token?.data.count > 0) token.data.count--;
  return token?.value
}

/**
 * 等待Token赋值
 */
export function waitForToken() {
  return PromiseUtils.waitFor(() => getToken());
}

/**
 * 设置Token
 */
export function setupToken(token: string | Token,
  type: TokenType = DefaultTokenType, save = true) {
  token = Token.create(token);
  if (!token.isValid()) return;

  if (save) console.log("setupToken", token, type)

  _tokens[type] = token;

  // 有次数限制就不保存
  // @ts-ignore
  save = save && (token.data.count == -1 || token.data.count == undefined);
  if (save) setLocalStorage(`${TokenKey}-${type}`, token.value);
}

export function removeToken(type: TokenType = DefaultTokenType) {
  delete _tokens[type];
  removeLocalStorage(`${TokenKey}-${type}`);
}

/**
 * 清空Token
 */
export function clearToken() {
  // @ts-ignore
  Object.keys(_tokens).forEach(removeToken)

  // for (const type of Object.keys(_tokens)) {
  //   delete _tokens[type];
  //   removeLocalStorage(`${TokenKey}-${type}`);
  // }
}

// endregion

// JWT操作

/**
 * Key签发
 */
export function generateKey(payload: Payload) {
  return JWT.sign(payload, JWTKey, { expiresIn: JWTExpireTime });
}

/**
 * Key校验
 */
export function verifyKey(key: any) {
  let res: {
    success: boolean, errMsg?: string, payload?: Payload
  };
  // @ts-ignore
  JWT.verify(key, JWTKey, (err: { message: any; }, decoded: { iat: any; exp: any; }) => {
    res = err ?
      { success: false, errMsg: err.message } :
      { success: true, payload: decoded };
    if (!err) {
      delete decoded.iat;
      delete decoded.exp;
    }
  })
  // @ts-ignore
  return res;
}

// endregion
