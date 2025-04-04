import type { RuleObject } from 'ant-design-vue/lib/form/interface';
import { ref, computed, unref, Ref } from 'vue';
import { useI18n } from '/@/hooks/web/useI18n';

export enum LoginStateEnum {
  LOGIN,
  REGISTER,
  RESET_PASSWORD,
  MOBILE,
  QR_CODE,
}

const currentState = ref(LoginStateEnum.LOGIN);

export function useLoginState() {
  function setLoginState(state: LoginStateEnum) {
    currentState.value = state;
  }

  const getLoginState = computed(() => currentState.value);

  function handleBackLogin() {
    setLoginState(LoginStateEnum.LOGIN);
  }

  return { setLoginState, getLoginState, handleBackLogin };
}

export function useFormValid<T extends object = any>(formRef: Ref<any>) {
  async function validForm() {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate();
    return data as T;
  }

  return { validForm };
}

export function useFormRules(formData?: Recordable) {
  const { t } = useI18n();

  const getAccountFormRule = computed(() => createRule(t('sys.login.accountPlaceholder')));
  const getPasswordFormRule = computed(() => createRule(t('sys.login.passwordPlaceholder')));
  const getSmsFormRule = computed(() => createRule(t('sys.login.smsPlaceholder')));
  const getMobileFormRule = computed(() => createRule(t('sys.login.mobilePlaceholder')));
  const getEmailFormRule = computed(() => createRule(t('sys.login.emailPlaceholder')));
  const getPwdQuestionAnswerFormRule = computed(() => createRule(t('sys.login.pwdQuestionAnswer')));
  const getUserNameFormRule = computed(() => createRule(t('sys.login.userNamePlaceholder')));
  // const getCorpRule = computed(() => createRule(t('sys.login.corpPlaceholder')));

  const validatePolicy = async (_: RuleObject, value: boolean) => {
    return !value ? Promise.reject(t('sys.login.policyPlaceholder')) : Promise.resolve();
  };

  const validateConfirmPassword = (password: string) => {
    return async (_: RuleObject, value: string) => {
      if (!value) {
        return Promise.reject(t('sys.login.passwordPlaceholder'));
      }
      if (value !== password) {
        return Promise.reject(t('sys.login.diffPwd'));
      }
      return Promise.resolve();
    };
  };

  const getFormRules = computed((): { [k: string]: RuleObject | RuleObject[] } => {
    const accountFormRule = unref(getAccountFormRule);
    const passwordFormRule = unref(getPasswordFormRule);
    const smsFormRule = unref(getSmsFormRule);
    const mobileFormRule = unref(getMobileFormRule);
    const emailFormRule = unref(getEmailFormRule);
    const pwdQuestionAnswerFormRule = unref(getPwdQuestionAnswerFormRule);
    const userNameFormRule = unref(getUserNameFormRule);
    // const corpRule = unref(getCorpRule);

    switch (unref(currentState)) {
      // register form rules
      case LoginStateEnum.REGISTER:
        return {
          loginCode: accountFormRule,
          userName: userNameFormRule,
          mobile: mobileFormRule,
          email: emailFormRule,
          validCode: smsFormRule,
          regValidCode: smsFormRule,
          password: passwordFormRule,
          confirmPassword: [{ validator: validateConfirmPassword(formData?.password), trigger: 'change' }],
          policy: [{ validator: validatePolicy, trigger: 'change' }],
        } as any;

      // reset password form rules
      case LoginStateEnum.RESET_PASSWORD:
        return {
          loginCode: accountFormRule,
          validCode: smsFormRule,
          fpValidCode: smsFormRule,
          password: passwordFormRule,
          confirmPassword: [{ validator: validateConfirmPassword(formData?.password), trigger: 'change' }],
          pwdQuestionAnswer: pwdQuestionAnswerFormRule,
          pwdQuestionAnswer2: pwdQuestionAnswerFormRule,
          pwdQuestionAnswer3: pwdQuestionAnswerFormRule,
        } as any;

      // mobile form rules
      case LoginStateEnum.MOBILE:
        return {
          validCode: smsFormRule,
          loginValidCode: smsFormRule,
          mobile: mobileFormRule,
        } as any;

      // login form rules
      default:
        return {
          account: accountFormRule,
          password: passwordFormRule,
          // corpCode: corpRule,
        } as any;
    }
  });
  return { getFormRules };
}

function createRule(message: string) {
  return [
    {
      required: true,
      message,
      trigger: 'change',
    },
  ];
}
