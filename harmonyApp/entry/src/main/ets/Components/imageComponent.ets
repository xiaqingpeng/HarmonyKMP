import { fileUri } from '@kit.CoreFileKit';

interface ImageArgs {
  Uri: string;
}

@Component
export struct imageComponent {
  @Consume compose_args: ImageArgs;
  @State imageUri: ResourceStr = '';

  private normalizeUri(src: string): ResourceStr {
    if (!src || src.length === 0) return '';
    if (
      src.startsWith('file://') ||
      src.startsWith('resource://') ||
      src.startsWith('http://') ||
      src.startsWith('https://')
    ) {
      return src;
    }
    return fileUri.getUriFromPath(src);
  }

  aboutToAppear(): void {
    const src = this.compose_args?.Uri ?? '';
    this.imageUri = this.normalizeUri(src);
  }

  aboutToDisappear(): void {
    this.imageUri = '';
  }

  build() {
    if (!this.imageUri) {
      Stack()
        .width('100%')
        .aspectRatio(1)
        .backgroundColor(0xFF111111);
    } else {
      Image(this.imageUri)
        .sourceSize({ width: 256, height: 256 })
        .width('100%')
        .aspectRatio(1)
        .onDisAppear(() => {
          this.imageUri = '';
        });
    }
  }
}
