import { ErrorTypeEnum } from '@jeesite/core/enums/exceptionEnum';
import { MenuModeEnum, MenuTypeEnum } from '@jeesite/core/enums/menuEnum';
// import { RoleInfo } from '@jeesite/core/api/sys/model/userModel';

// Lock screen information
export interface LockInfo {
  // Password required
  pwd?: string | undefined;
  // Is it locked?
  isLock?: boolean;
}

// Error-log information
export interface ErrorLogInfo {
  // Type of error
  type: ErrorTypeEnum;
  // Error file
  file: string;
  // Error name
  name?: string;
  // Error message
  message: string;
  // Error stack
  stack?: string;
  // Error detail
  detail: string;
  // Error url
  url: string;
  // Error time
  time?: string;
}

export interface UserInfo {
  // userId: string | number;
  // username: string;
  // realName: string;
  // avatar: string;
  // desc?: string;
  userCode: string | number;
  loginCode: string;
  userName: string;
  avatarUrl: string;
  remarks?: string;
  roleList?: any[];
  postList?: any[];
  homePath?: string;
  // roles: RoleInfo[];
}

export interface BeforeMiniState {
  menuCollapsed?: boolean;
  menuSplit?: boolean;
  menuMode?: MenuModeEnum;
  menuType?: MenuTypeEnum;
}
