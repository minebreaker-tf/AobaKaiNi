# Aoba KaiNi

JVM向け軽量CMS


## 1. ルーティングルール

1. 予約ルート  
    [予約ルート](#2)参照

2. コンテンツ  
    リクエストされたURLのパスと、コンテンツディレクトリ内にある
    Markdownファイルの拡張子を除いたものが一致する場合、
    そのファイルをテンプレートとしてレスポンスページを作成し、返す。
    同名のJSONファイルを、ページの設定として使用する。
    
    <!-- TODO Not implemented yet
    その際同名のjsファイル、cssファイルが存在した場合、それらを読み込む。
    -->

    例:  
    `$content_dir/foo/bar.md`ファイルが存在するとき、`context/foo/bar`にリクエスト  

3. ファイル
    リクエストされたURLのパスと、コンテンツディレクトリ内にある
    ファイル名が一致する場合、そのファイルを返す。

4. 404
    404レスポンスを返す。
<!-- TODO Not implemented yet
    404.mdがある場合、それをテンプレートとして使用する。
-->


## 2. 予約ルート<a id="2">

以下のルート/ファイルはリクエストにマッピングされず、特殊な用途で使用されます。

#### /setting
`setting.json`  
アプリケーションの設定を保持

#### /index
`index.md`  
`index.js`  
`index.css`  
`index.json`  
インデックスページ。`/`に対してマッピングされる。

#### /internal
内部フォアワーディングに使用される。

<!-- TODO Not implemented yet
#### /default
`default.js`
`default.css`
存在する場合、全てのリクエストで読み込まれる。
-->

#### /404
`404.md`  
対応するルートがなかった場合

<!-- TODO Not implemented yet
#### /search
-->


## 3. Misc
### パンくずリスト
階下にindexファイルがある場合、パンくずリストのリンクが有効になる。
TODO ファイル名をコンフィグJSONから読むようにする

### サイトマップ
`/sitemap.txt`にテキスト・サイトマップが生成される
