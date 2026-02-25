import "./Header.css";

export default function Header({ onWrite, onLogout, onProfile }) {
    return (
        <header className="header">
            <div className="header-inner">
                <h1 className="logo">AI SNS</h1>
                <div className="actions">
                    <button type="button" onClick={onProfile}>👤 프로필</button>
                    <button type="button" onClick={onWrite}>✏ 글쓰기</button>
                    <button type="button" onClick={onLogout}>로그아웃</button>
                </div>
            </div>
        </header>

    );
}
