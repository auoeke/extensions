package net.auoeke.extensions

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes

class TreeMover(fromRoot: Path, toRoot: Path) : TreeCopier(fromRoot, toRoot) {
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        file.move(this.newPath(file), StandardCopyOption.REPLACE_EXISTING)

        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(dir: Path, exc: IOException): FileVisitResult {
        dir.delete()

        return FileVisitResult.CONTINUE
    }
}
