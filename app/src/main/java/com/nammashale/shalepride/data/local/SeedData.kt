package com.nammashale.shalepride.data.local

import com.nammashale.shalepride.data.local.entity.*
import com.nammashale.shalepride.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SeedData {

    // Using picsum.photos - reliable, no API key needed, always returns images
    // Format: https://picsum.photos/seed/{keyword}/800/500

    fun populateIfEmpty(database: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val facilityDao = database.facilityDao()
            val starDao     = database.studentStarDao()
            val postDao     = database.postDao()

            // ─── FACILITIES ───────────────────────────────────────────────
            val facilities = listOf(
                FacilityEntity(
                    name = "Smart Classroom",
                    description = "Equipped with projector, interactive smart board, and digital learning tools. Students enjoy multimedia lessons every day.",
                    imageUrl = "https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=800",
                    category = "Smart Class"
                ),
                FacilityEntity(
                    name = "School Library",
                    description = "A well-stocked library with 5,000+ books covering all subjects, Kannada literature, and encyclopedias. Open daily 8 AM–5 PM.",
                    imageUrl = "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=800",
                    category = "Library"
                ),
                FacilityEntity(
                    name = "Science Laboratory",
                    description = "Fully equipped science lab for physics, chemistry, and biology experiments. Recently upgraded with new microscopes and safety equipment.",
                    imageUrl = "https://images.unsplash.com/photo-1532094349884-543559c0864b?w=800",
                    category = "Labs"
                ),
                FacilityEntity(
                    name = "Playground & Sports Ground",
                    description = "Large playground with cricket pitch, football field, kabaddi court, and athletics track. Coached sports sessions every evening.",
                    imageUrl = "https://images.unsplash.com/photo-1526232761682-d26e03ac148e?w=800",
                    category = "Playground"
                ),
                FacilityEntity(
                    name = "Mid-Day Meal Kitchen",
                    description = "Hygienic and modern kitchen facility preparing nutritious meals for 800+ students daily. Complies with FSSAI food safety standards.",
                    imageUrl = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800",
                    category = "Smart Class"
                ),
                FacilityEntity(
                    name = "Computer Lab",
                    description = "30-system computer lab with high-speed internet. Students learn coding, digital literacy, and use government e-learning portals.",
                    imageUrl = "https://images.unsplash.com/photo-1588702547923-7093a6c3ba33?w=800",
                    category = "Labs"
                )
            )
            for (f in facilities) {
                try { facilityDao.insertFacility(f) } catch (_: Exception) {}
            }

            // ─── STUDENT STARS ─────────────────────────────────────────────
            val now = System.currentTimeMillis()
            val day = 86_400_000L
            val stars = listOf(
                StudentStarEntity(
                    name = "Priya Sharma",
                    className = "Class 8A",
                    achievement = "🥇 Won 1st place in District Level Science Quiz. Represented the school at State Level.",
                    photoUrl = "https://picsum.photos/seed/priya2025/300/300",
                    timestamp = now
                ),
                StudentStarEntity(
                    name = "Ravi Kumar",
                    className = "Class 10B",
                    achievement = "♟ State Level Chess Champion — Gold Medal. Undefeated in 18 consecutive matches.",
                    photoUrl = "https://picsum.photos/seed/ravi2025/300/300",
                    timestamp = now - day
                ),
                StudentStarEntity(
                    name = "Anjali Patil",
                    className = "Class 7A",
                    achievement = "✍️ Best Essay Writer — Rajyotsava Competition. Essay published in district newsletter.",
                    photoUrl = "https://picsum.photos/seed/anjali2025/300/300",
                    timestamp = now - (2 * day)
                ),
                StudentStarEntity(
                    name = "Suresh Gowda",
                    className = "Class 9B",
                    achievement = "🏃 District Athletics Gold — 100m sprint. Selected for State School Games 2025.",
                    photoUrl = "https://picsum.photos/seed/suresh2025/300/300",
                    timestamp = now - (3 * day)
                ),
                StudentStarEntity(
                    name = "Deepa Nair",
                    className = "Class 6A",
                    achievement = "🎨 Best Drawing Award — National Children's Art Competition. Won ₹5,000 scholarship.",
                    photoUrl = "https://picsum.photos/seed/deepa2025/300/300",
                    timestamp = now - (4 * day)
                )
            )
            for (s in stars) {
                try { starDao.insertStudentStar(s) } catch (_: Exception) {}
            }

            // ─── POSTS / ACTIVITY FEED ────────────────────────────────────
            val posts = listOf(
                PostEntity(
                    title = "Annual Sports Day 2025 🏆",
                    description = "Our Annual Sports Day was a grand success! Students competed in 15+ events including running, high jump, and team sports. Congratulations to all winners and participants.",
                    imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=800&q=80",
                    type = Constants.POST_TYPE_ACTIVITY,
                    authorName = "School Admin",
                    timestamp = now - (1 * day)
                ),
                PostEntity(
                    title = "SSLC Results 2025 🎉",
                    description = "We are proud to announce that 98% of our Class 10 students passed the SSLC board exams! 12 students scored above 90%. Heartfelt congratulations to all!",
                    imageUrl = "https://images.unsplash.com/photo-1627556704302-624286467c65?w=800&q=80",
                    type = Constants.POST_TYPE_ACHIEVEMENT,
                    authorName = "School Admin",
                    timestamp = now - (2 * day)
                ),
                PostEntity(
                    title = "Today's Mid-Day Meal 🍱",
                    description = "Today's nutritious meal: Rice, Sambar, Mixed Vegetable Palya, and Curd. Our kitchen prepared fresh hot meals for 847 students. Thank you to our dedicated kitchen staff!",
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&q=80",
                    type = Constants.POST_TYPE_MEAL,
                    authorName = "School Admin",
                    timestamp = now - (3 * day)
                ),
                PostEntity(
                    title = "New Computer Lab Inaugurated 💻",
                    description = "Our school's new 30-system Computer Lab was inaugurated by the Block Education Officer. Students will now have access to high-speed internet and coding resources.",
                    imageUrl = "https://images.unsplash.com/photo-1588702547923-7093a6c3ba33?w=800&q=80",
                    type = Constants.POST_TYPE_ANNOUNCEMENT,
                    authorName = "School Admin",
                    timestamp = now - (4 * day)
                ),
                PostEntity(
                    title = "Kannada Rajyotsava Celebration 🎭",
                    description = "Students performed cultural programs including folk dances, Yakshagana, and speeches to celebrate Karnataka's 68th Rajyotsava. A wonderful display of our rich culture!",
                    imageUrl = "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?w=800&q=80",
                    type = Constants.POST_TYPE_ACTIVITY,
                    authorName = "School Admin",
                    timestamp = now - (5 * day)
                ),
                PostEntity(
                    title = "Parent-Teacher Meeting — Dec 2025 📢",
                    description = "Dear Parents, our quarterly PTM is scheduled for December 20, 2025 from 10 AM to 1 PM. Your child's progress reports will be shared. Please confirm attendance.",
                    imageUrl = "https://images.unsplash.com/photo-1577896851231-70ef18881754?w=800&q=80",
                    type = Constants.POST_TYPE_ANNOUNCEMENT,
                    authorName = "School Admin",
                    timestamp = now - (6 * day)
                )
            )
            for (p in posts) {
                try { postDao.insertPost(p) } catch (_: Exception) {}
            }
        }
    }
}
