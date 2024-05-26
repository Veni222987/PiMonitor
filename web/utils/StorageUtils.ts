// import CryptoJS from "crypto-js";
//
// // 加密密钥
// const encryptionKey = process.env.SECRET_KEY || "default-encryption-key";
//
// // 加密函数
// const encryptNumber = (number: number): string => {
//   return CryptoJS.AES.encrypt(String(number), encryptionKey).toString();
// };
//
// // 解密函数
// const decryptNumber = (encryptedData: string): number => {
//   const decryptedBytes = CryptoJS.AES.decrypt(encryptedData, encryptionKey);
//   const decryptedNumber = CryptoJS.enc.Utf8.stringify(decryptedBytes);
//   return Number(decryptedNumber);
// };
//
// // 存储加密后的数字到 localStorage
// export const saveEncryptedNumber = (key: string, number: number) => {
//   const encryptedData = encryptNumber(number);
//   setLocalStorage(key, encryptedData)
// };
//
// // 从 localStorage 中获取加密的数字并解密
// export const getDecryptedNumber = (key: string): number | null => {
//   const encryptedData = getLocalStorage(key);
//   if (encryptedData) {
//     return decryptNumber(encryptedData);
//   }
//   return null;
// };
export function setLocalStorage<T>(key: string, data: T) {
  let strData;
  switch (typeof data) {
    case "object": strData = JSON.stringify(data); break;
    case "boolean": strData = data ? "true" : "false"; break;
    default: strData = (data as any)?.toString(); break;
  }
  window?.localStorage?.setItem(key, strData);
  return data as T
}
export function getLocalStorage<T = string>(key: string, type?: any): T | null {
  if (typeof window === 'undefined') {
    return null; // 在服务器端返回 null 或者其他默认值
  }

  const data = window?.localStorage?.getItem(key);
  if (data === null) return null;
  switch (type) {
    case Object: return JSON.parse(data);
    case Number: return Number(data) as T;
    case Boolean: return (data && data != "false") as T;
    default: return data as T;
  }
}
export function getOrSetLocalStorage<T>(key: string, data: T | (() => T)) {
  const res = getLocalStorage<T>(key);
  return res !== null ? res :
      setLocalStorage<T>(key, data instanceof Function ? data() : data)
}

export function removeLocalStorage(key: string) {
  if (typeof window === 'undefined') {
    return null; // 在服务器端返回 null 或者其他默认值
  }
  window?.localStorage?.removeItem(key);
}
