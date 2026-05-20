import { ArkUIViewController } from "compose/src/main/cpp/types/libcompose_arkui_utils";

export const MainArkUIViewController: () => ArkUIViewController
export const initResourceManager: (resourceManager: resourceManager.ResourceManager) => number;
export const sendBoxImagePath: (path: string) => void;
export const sendImagePathList: (paths: Array<string>) => void;
export const sendKotlinCallArkTsController: (controller: object) => void;
export const arkTsCallKotlin: (input: number) => string;
