import { Result } from '../utils';

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

export default class UserService {
  async login(ctx) {
    const { username, password } = ctx.request.query;
    const checkUser = fakeUserList.find((item) => item.username === username && password === item.password);
    if (!checkUser) {
      return Result.error('Incorrect account or password！');
    }
    const { userCode, username: _username, token, realName, desc, roles } = checkUser;
    return Result.success({
      roles,
      userCode,
      username: _username,
      token,
      realName,
      desc,
    });
  }

  async index(ctx) {
    const { token } = ctx.request.query;
    if (!token) return Result.error('Invalid token');
    const checkUser = fakeUserList.find((item) => item.token === token);
    if (!checkUser) {
      return Result.error('The corresponding user information was not obtained!');
    }
    return Result.success(fakeUserList);
  }
}
