import { resultError, resultSuccess, getRequestToken, RequestParams } from '../mockUtils';
import { MockMethod } from 'vite-plugin-mock';

export const fakeUserList: any = [
  {
    userCode: '1',
    username: 'c3lzdGVt',
    password: 'YWRtaW4=',
    userName: 'ThinkGem',
    avatarUrl: 'https://q1.qlogo.cn/g?b=qq&nk=190848757&s=640',
    remarks: 'manager',
    token: 'fakeToken1',
    roleList: [
      {
        roleName: 'Super Admin',
        roleCode: 'super',
      },
    ],
  },
  {
    userCode: '2',
    username: 'test',
    password: '123456',
    userName: 'TestUser',
    avatarUrl: 'https://q1.qlogo.cn/g?b=qq&nk=339449197&s=640',
    remarks: 'tester',
    token: 'fakeToken2',
    roleList: [
      {
        roleName: 'Tester',
        roleCode: 'test',
      },
    ],
  },
];

export const fakeCodeList: any = {
  '1': ['1000', '3000', '5000'],
  '2': ['2000', '4000', '6000'],
};

export default [
  {
    url: '/js/a/demo/login',
    timeout: 200,
    method: 'get',
    response: ({ body, query }) => {
      const username = body.username || query.username;
      const password = body.password || query.password;
      const checkUser = fakeUserList.find(
        (item) => item.username === username && password === item.password,
      );
      if (!checkUser) {
        return resultError('Incorrect account or password！');
      }
      const { userCode, username: _username, token, realName, desc, roles } = checkUser;
      return resultSuccess({
        roles,
        userCode,
        username: _username,
        token,
        realName,
        desc,
      });
    },
  },
  {
    url: '/js/a/demo/index',
    method: 'get',
    response: (request: RequestParams | any) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = fakeUserList.find((item) => item.token === token);
      if (!checkUser) {
        return resultError('The corresponding user information was not obtained!');
      }
      return resultSuccess(checkUser);
    },
  },
  {
    url: '/js/a/demo/authInfo',
    timeout: 200,
    method: 'get',
    response: (request: RequestParams | any) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = fakeUserList.find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      const codeList = fakeCodeList[checkUser.userCode];
      return resultSuccess(codeList);
    },
  },
  {
    url: '/js/a/demo/logout',
    timeout: 200,
    method: 'get',
    response: (request: RequestParams | any) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = fakeUserList.find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      return resultSuccess(undefined, { message: 'Token has been destroyed' });
    },
  },
] as MockMethod[];
