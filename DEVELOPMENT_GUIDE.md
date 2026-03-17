# 🚀 Suhbat Development Guide

## 📋 Quick Start - Git Setup

### Create Main Branch & Push Changes

```bash
# Navigate to project
cd d:\Telesuhbat

# 1. Create main branch from current rewrite branch
git checkout -b main

# 2. Update remote to YOUR repository
git remote set-url origin https://github.com/Bahtiyorjon05/Telesuhbat.git

# 3. Verify remote
git remote -v
# Should show:
# origin  https://github.com/Bahtiyorjon05/Telesuhbat.git (fetch)
# origin  https://github.com/Bahtiyorjon05/Telesuhbat.git (push)

# 4. Add all changes
git add .

# 5. Commit with message
git commit -m "Rebrand to Suhbat - Initial release ready for Play Store

- Changed all user-facing strings from AyuGram/exteraGram to Suhbat
- Updated package name to uz.suhbat.app
- Configured Firebase with google-services.json
- Updated README with Suhbat branding
- Added proper credits to original authors
- Fixed API_KEYS configuration
- Enhanced .gitignore for security"

# 6. Push to GitHub
git push -u origin main

# 7. Verify on GitHub
# Visit: https://github.com/Bahtiyorjon05/Telesuhbat
```

### Daily Workflow

```bash
# Check what changed
git status

# Add specific files
git add path/to/file

# Or add everything
git add .

# Commit
git commit -m "Description of changes"

# Push
git push origin main
```

### Creating Feature Branches

```bash
# Start new feature
git checkout -b feature/ghost-mode-improvements

# ... code ...

# Commit feature
git add .
git commit -m "Improve ghost mode with new options"

# Merge to main
git checkout main
git merge feature/ghost-mode-improvements
git push origin main

# Delete feature branch
git branch -d feature/ghost-mode-improvements
```

---

## 🔐 Security Checklist

### ✅ Protected Files (In .gitignore)

These files are **NEVER** committed to Git:

- `API_KEYS` - Contains your API credentials
- `google-services.json` - Firebase configuration
- `*.jks`, `*.keystore` - Signing keys
- `local.properties` - Local SDK paths
- `build/`, `.gradle/`, `.idea/` - Build artifacts

### ⚠️ What You Did Right

1. ✅ API_KEYS is in .gitignore
2. ✅ google-services.json is in .gitignore
3. ✅ Keystore files are in .gitignore
4. ✅ Build folders are in .gitignore

---

## 📱 Current Build Configuration

### Versions (STABLE - Don't Change Yet)

```properties
Gradle: 7.5.1
Android Gradle Plugin: 7.4.2
Java: 11
Compile SDK: 33
Target SDK: 33
Min SDK: 21
```

### Device Coverage

- ✅ **Android 5.0 (2014)** to **Android 15 (2024)**
- ✅ **97% of all Android devices**
- ✅ **Budget phones** to **Flagship phones**

---

## 🎯 Feature Ideas for Suhbat

### Priority 1: Essential Features (Start Here)

#### 1. **Enhanced Ghost Mode** 👻
```
Location: TMessagesProj/src/main/java/com/radolyn/ayugram/utils/AyuGhostUtils.java

Ideas:
- Per-chat ghost mode (hide from specific people only)
- Scheduled ghost mode (auto-enable at certain times)
- Ghost mode exceptions (always show online to favorites)
- Fake "last seen" timestamp
```

#### 2. **Message History Improvements** 📜
```
Location: TMessagesProj/src/main/java/com/radolyn/ayugram/ui/AyuMessageHistory.java

Ideas:
- Search in deleted messages
- Export deleted messages to file
- Filter by media type in history
- Restore deleted messages to chat
```

#### 3. **Custom Themes** 🎨
```
Location: TMessagesProj/src/main/java/org/telegram/ui/ThemeActivity.java

Ideas:
- Theme editor with live preview
- Import/export themes
- Gradient chat backgrounds
- Custom message bubble shapes
```

### Priority 2: Privacy Features

#### 4. **App Lock** 🔒
```
New File: TMessagesProj/src/main/java/org/telegram/ui/Privacy/AppLockActivity.java

Features:
- Fingerprint unlock
- PIN/Pattern lock
- Fake crash on wrong password
- Lock specific chats
- Incognito mode
```

#### 5. **Anti-Delete** 🛡️
```
Location: TMessagesProj/src/main/java/com/radolyn/ayugram/messages/AyuMessagesController.java

Enhancements:
- Prevent others from deleting their messages in your chat
- Auto-save before deletion
- Notify when someone deletes a message
- Screenshot protection for secret chats
```

#### 6. **Message Scheduler** ⏰
```
New File: TMessagesProj/src/main/java/org/telegram/ui/ScheduledMessagesActivity.java

Features:
- Send message at specific time
- Recurring messages (daily/weekly)
- Send when user comes online
- Location-based triggers
```

### Priority 3: UI/UX Improvements

#### 7. **Font Customization** 🔤
```
Location: TMessagesProj/src/main/java/org/telegram/ui/Settings/ChatSettingsActivity.java

Features:
- Custom font selector
- Font size slider (beyond Telegram's limits)
- Bold text everywhere toggle
- Monospace font option
```

#### 8. **Chat Folders++** 📁
```
Enhancement of existing folders

Features:
- Unlimited folders (Telegram limits to 10)
- Folder icons customization
- Auto-sort rules
- Folder passwords
```

#### 9. **Notification Profiles** 🔔
```
New File: TMessagesProj/src/main/java/org/telegram/ui/NotificationProfilesActivity.java

Features:
- Different sounds per contact
- Quiet hours schedule
- Smart notifications (only from favorites)
- Notification LED colors
- Vibration patterns
```

### Priority 4: Advanced Features

#### 10. **Dual Accounts++** 👥
```
Enhancement of existing account switcher

Features:
- More than 3 accounts
- Quick account switch from notification
- Different notifications per account
- Account-specific themes
```

#### 11. **Download Manager** ⬇️
```
New File: TMessagesProj/src/main/java/org/telegram/ui/DownloadManagerActivity.java

Features:
- Download queue management
- Resume interrupted downloads
- Download speed limiter
- Auto-delete downloaded media
- Wi-Fi only mode per chat
```

#### 12. **Backup & Restore** 💾
```
New File: TMessagesProj/src/main/java/org/telegram/ui/BackupActivity.java

Features:
- Export chats to PDF/HTML
- Backup to Google Drive
- Export media separately
- Encrypted backups
- Restore from backup
```

### Priority 5: Suhbat Exclusive

#### 13. **SuhbatSync Improvements** 🔄
```
Location: TMessagesProj/src/main/java/com/radolyn/ayugram/sync/

Enhancements:
- End-to-end encrypted sync
- Selective sync (only messages/media)
- Sync conflicts resolver
- Multi-device sync status
- Sync over LAN
```

#### 14. **Message Filters++** 🔍
```
Location: TMessagesProj/src/main/java/com/radolyn/ayugram/AyuFilter.java

Enhancements:
- AI-powered spam filter
- Auto-delete promotional messages
- Keyword highlighting
- Filter by sender type (bot/user/channel)
- Auto-archive rules
```

#### 15. **Media Editor** 📷
```
New File: TMessagesProj/src/main/java/org/telegram/ui/MediaEditorActivity.java

Features:
- Image editor before sending
- Video trimmer
- Add watermarks
- Blur faces/text
- Meme generator
```

---

## 🛠️ How to Add New Features

### Step 1: Plan
```
1. Choose feature from list above
2. Identify which files to modify
3. Create new file if needed
4. Test in Android Studio
```

### Step 2: Code Structure
```java
// Example: New feature file structure
// File: TMessagesProj/src/main/java/org/telegram/ui/SuhbatFeatureActivity.java

package org.telegram.telegram.ui;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;

public class SuhbatFeatureActivity extends BaseFragment {
    
    public SuhbatFeatureActivity() {
        super();
    }
    
    @Override
    public View createView(Context context) {
        // Create your UI here
        return super.createView(context);
    }
    
    @Override
    public boolean onFragmentCreate() {
        // Initialize data
        return super.onFragmentCreate();
    }
}
```

### Step 3: Add to Settings
```java
// Add menu item in ProfileActivity.java or SettingsActivity.java

TextCell textCell = new TextCell(context);
textCell.setText(LocaleController.getString("SuhbatFeature", R.string.SuhbatFeature));
textCell.setOnClickListener(v -> {
    presentFragment(new SuhbatFeatureActivity());
});
listAdapter.add(textCell);
```

### Step 4: Add Strings
```xml
<!-- Add to: TMessagesProj/src/main/res/values/ayu.xml -->

<string name="SuhbatFeature">Feature Name</string>
<string name="SuhbatFeatureDescription">What it does</string>
```

### Step 5: Test & Commit
```bash
git add .
git commit -m "feat: Add new Suhbat feature

- Description of what was added
- Files modified
- Testing done"
git push origin main
```

---

## 📚 Resources

### Official Documentation
- [Telegram API](https://core.telegram.org/api)
- [Android Developers](https://developer.android.com)
- [Firebase Setup](https://firebase.google.com/docs/android/setup)

### Code References
- Existing Suhbat code: `TMessagesProj/src/main/java/com/radolyn/ayugram/`
- Telegram base: `TMessagesProj/src/main/java/org/telegram/`
- Extera features: `TMessagesProj/src/main/java/com/exteragram/messenger/`

### Build Commands
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleAfatRelease

# Build all variants
./gradlew assembleRelease

# Run tests
./gradlew test
```

---

## 🎯 Next Steps

1. ✅ Run git commands above to create main branch
2. ✅ Push changes to your GitHub
3. ✅ Choose first feature to implement
4. ✅ Start coding!
5. ✅ Test thoroughly
6. ✅ Release to Play Store

---

## 💡 Pro Tips

### Code Quality
- Always test on real devices
- Keep Min SDK 21 compatibility
- Don't break existing features
- Comment your code
- Follow existing code style

### Git Best Practices
- Commit often with clear messages
- Use feature branches for big changes
- Pull before pushing
- Keep main branch stable

### Play Store
- Update version code each release
- Write clear changelogs
- Test on multiple Android versions
- Respond to user reviews

---

**Good luck with Suhbat! 🚀**

*Remember: Stable code > Latest versions*
