import { Result } from '../utils';

const fakeUserInfo = {
  userCode: '1',
  username: 'system',
  realName: 'ThinkGem',
  password: 'admin',
  desc: 'manager',
  token: 'fakeToken1',
  roles: [
    {
      roleName: 'Super Admin',
      value: 'super',
    },
  ],
};
export default class UserService {
  async login() {
    return Result.success(fakeUserInfo);
  }

  async getUserInfoById() {
    return Result.success(fakeUserInfo);
  }
}
